package com.web.parallel;

import com.web.BrowserDetails;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.*;
import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.Keys.TAB;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class BaseTest {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "http://hub-cloud.browserstack.com/wd/hub";
    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    public static WebDriver getWebDriver() {
        return driverThread.get();
    }

    @BeforeSuite(alwaysRun = true)
    public void setupRestApi() {
        PreemptiveBasicAuthScheme authenticationScheme = new PreemptiveBasicAuthScheme();
        authenticationScheme.setUserName(USERNAME);
        authenticationScheme.setPassword(ACCESS_KEY);
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api-cloud.browserstack.com")
                .setBasePath("automate")
                .setAuth(authenticationScheme)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters("environment")
    public void setup(@Optional("desktop") String environment, Method m) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Random Browsers");
        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);
        List<BrowserDetails> browsers = get("browsers.json")
                .jsonPath()
                .getList("", BrowserDetails.class);
        int randomNumber;
        BrowserDetails browserDetails;
        switch (environment) {
            case "desktop":
                List<BrowserDetails> desktopBrowsers = browsers.parallelStream()
                        .filter(browser -> browser.getDevice() == null)
                        .collect(toList());
                randomNumber = ThreadLocalRandom.current().nextInt(0, desktopBrowsers.size());
                browserDetails = desktopBrowsers.get(randomNumber);
                System.out.println(browserDetails);
                caps.setCapability("build", "Random Desktop Browsers - " + TIMESTAMP);
                caps.setCapability("name", m.getName() + " - " + browserDetails.getBrowser() + " " + browserDetails.getBrowser_version());
                caps.setCapability("os", browserDetails.getOs());
                caps.setCapability("os_version", browserDetails.getOs_version());
                caps.setCapability("browser", browserDetails.getBrowser());
                caps.setCapability("browser_version", browserDetails.getBrowser_version());
                break;
            case "mobile":
                List<BrowserDetails> mobileBrowsers = browsers.parallelStream()
                        .filter(BrowserDetails::isReal_mobile)
                        .collect(toList());
                randomNumber = ThreadLocalRandom.current().nextInt(0, mobileBrowsers.size());
                browserDetails = mobileBrowsers.get(randomNumber);
                System.out.println(browserDetails);
                caps.setCapability("build", "Random Mobile Browsers - " + TIMESTAMP);
                caps.setCapability("name", m.getName() + " - " + browserDetails.getDevice());
                caps.setCapability("os_version", browserDetails.getOs_version());
                caps.setCapability("device", browserDetails.getDevice());
                caps.setCapability("browser", browserDetails.getBrowser());
                caps.setCapability("real_mobile", browserDetails.isReal_mobile());
                break;
            default:
                throw new IllegalArgumentException("Incorrect environment " + environment + " specified");
        }
        driverThread.set(new RemoteWebDriver(new URL(URL), caps));
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        JavascriptExecutor js = (JavascriptExecutor) driverThread.get();
        js.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
        driverThread.get().quit();
        driverThread.remove();
    }

    protected void testBStackDemoLogin() {
        WebDriver driver = getWebDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("https://bstackdemo.com");
        wait.until(elementToBeClickable(By.id("signin"))).click();
        wait.until(elementToBeClickable(By.cssSelector("#username input"))).sendKeys("fav_user" + TAB);
        driver.findElement(By.cssSelector("#password input")).sendKeys("testingisfun99" + TAB);
        driver.findElement(By.id("login-btn")).click();
        String username = wait.until(presenceOfElementLocated(By.className("username"))).getText();
        Assert.assertEquals(username, "fav_user", "Incorrect username");
    }

}
