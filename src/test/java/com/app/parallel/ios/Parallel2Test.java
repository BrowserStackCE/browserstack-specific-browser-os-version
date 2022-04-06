package com.app.parallel.ios;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Parallel2Test extends BaseTest {

    @Test
    public void printText3() {
        MobileDriver<MobileElement> driver = getMobileDriver();
        driver.findElementByAccessibilityId("Text Button").click();
        driver.findElementByAccessibilityId("Text Input").click();
        driver.findElementByAccessibilityId("Text Input").sendKeys("Welcome to BrowserStack" + Keys.ENTER);
        assertEquals(driver.findElementByAccessibilityId("Text Output").getText(),
                "Welcome to BrowserStack", "Incorrect text");
    }

    @Test
    public void printText4() {
        MobileDriver<MobileElement> driver = getMobileDriver();
        driver.findElementByAccessibilityId("Text Button").click();
        driver.findElementByAccessibilityId("Text Input").click();
        driver.findElementByAccessibilityId("Text Input").sendKeys("Welcome to BrowserStack" + Keys.ENTER);
        assertEquals(driver.findElementByAccessibilityId("Text Output").getText(),
                "Welcome to BrowserStack", "Incorrect text");
    }
}
