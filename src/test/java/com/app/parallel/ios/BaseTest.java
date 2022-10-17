package com.app.parallel.ios;

import com.app.DeviceDetails;
import com.util.AppUtils;
import com.util.SessionStatus;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.get;
import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertEquals;

public class BaseTest {

    private static final ThreadLocal<MobileDriver<MobileElement>> driverThread = new ThreadLocal<>();

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "https://hub-cloud.browserstack.com/wd/hub";
    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    public static MobileDriver<MobileElement> getMobileDriver() {
        return driverThread.get();
    }

    @BeforeSuite(alwaysRun = true)
    public void setupApp() {
        AppUtils.uploadApp("iOSDemoApp", "ios/BStackSampleApp.ipa");
    }

    @BeforeMethod(alwaysRun = true)
    public void setupDriver(Method m) throws MalformedURLException {
        List<DeviceDetails> mobileDevices = get("devices.json")
                .jsonPath()
                .getList("", DeviceDetails.class);
        List<DeviceDetails> iosDevices = mobileDevices.stream()
                .filter(device -> device.getOs().equals("ios"))
                .filter(device -> !device.getOs_version().contains("Beta"))
                .filter(device -> Integer.parseInt(device.getOs_version()) >= 14)
                .collect(toList());
        int randomNumber = ThreadLocalRandom.current().nextInt(0, iosDevices.size());
        DeviceDetails deviceDetails = iosDevices.get(randomNumber);
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Random Devices");
        caps.setCapability("build", "Random iOS Devices - " + TIMESTAMP);
        caps.setCapability("name", m.getName());
        caps.setCapability("os", deviceDetails.getOs());
        caps.setCapability("os_version", deviceDetails.getOs_version());
        caps.setCapability("device", deviceDetails.getDevice());
        caps.setCapability("app", "iOSDemoApp");
        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);

        driverThread.set(new IOSDriver<>(new URL(URL), caps));
    }

    @AfterMethod(alwaysRun = true)
    public void teardownDriver(ITestResult tr) {
        SessionStatus.markTestSessionStatus(driverThread.get(), tr);
        driverThread.get().quit();
        driverThread.remove();
    }

    protected void printText() {
        MobileDriver<MobileElement> driver = getMobileDriver();
        driver.findElementByAccessibilityId("Text Button").click();
        driver.findElementByAccessibilityId("Text Input").click();
        driver.findElementByAccessibilityId("Text Input").sendKeys("Welcome to BrowserStack" + Keys.ENTER);
        assertEquals(driver.findElementByAccessibilityId("Text Output").getText(),
                "Welcome to BrowserStack", "Incorrect text");
    }

}
