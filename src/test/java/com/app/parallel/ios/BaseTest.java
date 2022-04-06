package com.app.parallel.ios;

import com.app.DeviceDetails;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.*;
import static java.util.stream.Collectors.toList;

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
        PreemptiveBasicAuthScheme authenticationScheme = new PreemptiveBasicAuthScheme();
        authenticationScheme.setUserName(USERNAME);
        authenticationScheme.setPassword(ACCESS_KEY);
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api-cloud.browserstack.com")
                .setBasePath("app-automate")
                .setAuth(authenticationScheme)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        List<String> customIds = get("recent_apps").jsonPath().getList("custom_id");
        if (customIds == null || !customIds.contains("iOSDemoApp")) {
            System.out.println("Uploading app...");
            given()
                    .header("Content-Type", "multipart/form-data")
                    .multiPart("url", "https://www.browserstack.com/app-automate/sample-apps/ios/BStackSampleApp.ipa", "text")
                    .param("custom_id", "iOSDemoApp")
                    .post("upload");
        } else {
            System.out.println("Using previously uploaded app...");
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setupDriver(Method m) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Random Devices");
        caps.setCapability("build", "Random iOS Devices - " + TIMESTAMP);
        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);
        List<DeviceDetails> iosDevices = get("devices.json")
                .jsonPath()
                .getList("", DeviceDetails.class)
                .stream()
                .filter(device -> device.getOs().equals("ios"))
                .collect(toList());
        int randomNumber = ThreadLocalRandom.current().nextInt(0, iosDevices.size());
        DeviceDetails deviceDetails = iosDevices.get(randomNumber);
        System.out.println(deviceDetails);
        caps.setCapability("name", m.getName() + " - " + deviceDetails.getDevice());
        caps.setCapability("os", deviceDetails.getOs());
        caps.setCapability("os_version", deviceDetails.getOs_version());
        caps.setCapability("device", deviceDetails.getDevice());
        caps.setCapability("app", "iOSDemoApp");

        driverThread.set(new IOSDriver<>(new URL(URL), caps));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        JavascriptExecutor js = (JavascriptExecutor) driverThread.get();
        js.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
        driverThread.get().quit();
        driverThread.remove();
    }

}
