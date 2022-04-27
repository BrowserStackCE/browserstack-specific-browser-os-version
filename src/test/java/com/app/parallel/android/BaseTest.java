package com.app.parallel.android;

import com.app.DeviceDetails;
import com.util.AppUtils;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.get;
import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertTrue;

public class BaseTest {

    private static final ThreadLocal<MobileDriver<MobileElement>> driverThread = new ThreadLocal<>();

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "http://hub-cloud.browserstack.com/wd/hub";
    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    public static MobileDriver<MobileElement> getMobileDriver() {
        return driverThread.get();
    }

    @BeforeSuite(alwaysRun = true)
    public void setupApp() {
        AppUtils.uploadApp("AndroidDemoApp", "android/WikipediaSample.apk");
    }

    @BeforeMethod(alwaysRun = true)
    public void setupDriver(Method m) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Random Devices");
        caps.setCapability("build", "Random Android Devices - " + TIMESTAMP);
        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);
        List<DeviceDetails> androidDevices = get("devices.json")
                .jsonPath()
                .getList("", DeviceDetails.class)
                .stream()
                .filter(device -> device.getOs().equals("android"))
                .collect(toList());
        int randomNumber = ThreadLocalRandom.current().nextInt(0, androidDevices.size());
        DeviceDetails deviceDetails = androidDevices.get(randomNumber);
        System.out.println(deviceDetails);
        caps.setCapability("name", m.getName() + " - " + deviceDetails.getDevice());
        caps.setCapability("os", deviceDetails.getOs());
        caps.setCapability("os_version", deviceDetails.getOs_version());
        caps.setCapability("device", deviceDetails.getDevice());
        caps.setCapability("app", "AndroidDemoApp");

        driverThread.set(new AndroidDriver<>(new URL(URL), caps));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        JavascriptExecutor js = (JavascriptExecutor) driverThread.get();
        js.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
        driverThread.get().quit();
        driverThread.remove();
    }

    protected void searchWikipedia() {
        MobileDriver<MobileElement> driver = getMobileDriver();
        Wait<MobileDriver<MobileElement>> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .ignoring(NotFoundException.class);
        driver.findElementByAccessibilityId("Search Wikipedia").click();
        MobileElement insertTextElement = wait.until(d -> d.findElementById("org.wikipedia.alpha:id/search_src_text"));
        insertTextElement.click();
        insertTextElement.sendKeys("BrowserStack");
        wait.until(d -> d.findElementByClassName("android.widget.ListView").isDisplayed());
        List<String> companyNames = driver.findElementsByClassName("android.widget.TextView")
                .stream().map(MobileElement::getText).collect(toList());
        assertTrue(companyNames.contains("BrowserStack"), "Company is not present in the list");
    }

}
