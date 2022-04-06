# BrowserStack Parallel Execution on Random Browser/Device <a href="https://www.browserstack.com/"><img src="https://www.vectorlogo.zone/logos/browserstack/browserstack-icon.svg" alt="BrowserStack" height="30"/></a> <a href="https://java.com"><img src="https://www.vectorlogo.zone/logos/java/java-icon.svg" alt="Java" height="30" /></a> <a href="https://www.selenium.dev/"><img src="https://seeklogo.com/images/S/selenium-logo-DB9103D7CF-seeklogo.com.png" alt="Selenium" height="30" /></a>

| Product | Status |
| --- | --- |
| Automate | [![BrowserStack Status](https://automate.browserstack.com/badge.svg?badge_key=UWVWdnBjWlJ2cG5xeEtuNlRUbWJHKzBUalNNUERTYWdiWkRXRFpsQXJyQT0tLWd3emNyaFlZejQvUHdqK2JUQUpZMEE9PQ==--94f9568f9d22c96f2ed9e0af0ec02ed987fcaf41)](https://automate.browserstack.com/public-build/UWVWdnBjWlJ2cG5xeEtuNlRUbWJHKzBUalNNUERTYWdiWkRXRFpsQXJyQT0tLWd3emNyaFlZejQvUHdqK2JUQUpZMEE9PQ==--94f9568f9d22c96f2ed9e0af0ec02ed987fcaf41) |
| App-Automate | [![BrowserStack Status](https://app-automate.browserstack.com/badge.svg?badge_key=TFRlb05YZy9RdWxLUm1MSEFTMWhteisrQTVXc0lJR2t1TUNMVFY3b3V3WT0tLUpNUHJDQ1NocDRYcWpnK0pla0N3S0E9PQ==--f8a3b33360709d7645baf9237a7ea8c934d2748d)](https://app-automate.browserstack.com/public-build/TFRlb05YZy9RdWxLUm1MSEFTMWhteisrQTVXc0lJR2t1TUNMVFY3b3V3WT0tLUpNUHJDQ1NocDRYcWpnK0pla0N3S0E9PQ==--f8a3b33360709d7645baf9237a7ea8c934d2748d) |


Test execution on random browser/device on BrowserStack.

## Using Maven

### Setup

- Clone the repo.
- Install dependencies.
  ```
  mvn compile
  ```
- Update the environment variables with your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings).

### Running your tests

#### Automate

- Run parallel tests on random desktop browsers.
  ```
  mvn -P desktop-browsers test
  ```
- Run parallel tests on random mobile browsers.
  ```
  mvn -P mobile-browsers test
  ```
- Print the entire list of browsers on console.
  ```
  mvn -P available-browsers test
  ```

#### App-Automate

- Run parallel tests on random android devices.
  ```
  mvn -P android-devices test
  ```
- Run parallel tests on random ios devices.
  ```
  mvn -P ios-devices test
  ```
- Print the entire list of mobile devices on console.
  ```
  mvn -P available-devices test
  ```

## Using Gradle

### Setup

* Clone the repo.
* Install dependencies.
  ```
  ./gradlew build
  ```
* Update the environment variables with your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings).

### Running your tests

#### Automate

- Run parallel tests on random desktop browsers.
  ```
  ./gradlew desktop-browsers
  ```
- Run parallel tests on random mobile browsers.
  ```
  ./gradlew mobile-browsers
  ```
- Print the entire list of browsers on console.
  ```
  ./gradlew available-browsers
  ```

#### App-Automate

- Run parallel tests on android devices.
  ```
  ./gradlew android-devices
  ```
- Run parallel tests on ios devices.
  ```
  ./gradlew ios-devices
  ```
- Print the entire list of mobile devices on console.
  ```
  ./gradlew available-devices
  ```

## Specific use case

You can customize the filters used according to any specific requirements that you have. I have provided specific use cases below as examples.

### Automate

- In case you want to run all your tests on Chrome browser with the Browser Version greater than 90.0 you can add the below filters in the desktop section of the [BaseTest](src/test/java/com/web/parallel/BaseTest.java).
  ```
  .filter(browser -> browser.getBrowser().equals("chrome"))
  .filter(browser -> Double.parseDouble(browser.getBrowser_version().substring(0, 4)) > 90.0)
  ```

- In case you want to run all your tests on Android mobile browser with the Android Version greater than 9.0 you can add the below filters in the mobile section of the [BaseTest](src/test/java/com/web/parallel/BaseTest.java).
  ```
  .filter(browser -> browser.getOs().equals("android"))
  .filter(browser -> Double.parseDouble(browser.getOs_version()) > 9.0)
  ```

### App-Automate

- In case you want to run all your tests on Android devices with the Android version greater than 9.0 you can add the below filter in the android section of the [BaseTest](src/test/java/com/app/parallel/android/BaseTest.java).
  ```
  .filter(device -> Double.parseDouble(device.getOs_version()) > 9.0)
  ```

- In case you want to run all your tests on iOS devices with the iOS version equal to 12 you can add the below filter in the iOS section of the [BaseTest](src/test/java/com/app/parallel/ios/BaseTest.java).
  ```
  .filter(device -> Integer.parseInt(device.getOs_version()) == 12)
  ```

## Notes
- You can view your Automate test results on the [BrowserStack Automate dashboard](https://automate.browserstack.com/).
- You can view your App-Automate test results on the [BrowserStack App-Automate dashboard](https://app-automate.browserstack.com/).
- App used to test on mobile devices:
  - AndroidDemoApp: [WikipediaSample.apk](https://www.browserstack.com/app-automate/sample-apps/android/WikipediaSample.apk)
  - iOSDemoApp: [BStackSampleApp.ipa](https://www.browserstack.com/app-automate/sample-apps/ios/BStackSampleApp.ipa)
- To use specific number of threads:
  - For Maven use `-Dthreads=<thread-count>`. Example: `-Dthreads=3`
  - For Gradle use `-Pthreads=<thread-count>`. Example: `-Pthreads=3`
- You can export the environment variables for the Username and Access Key of your BrowserStack account
  ```sh
  export BROWSERSTACK_USERNAME=<browserstack-username> && export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
  ```
