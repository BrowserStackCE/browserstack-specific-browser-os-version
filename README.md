# BrowserStack Parallel Execution on Specific Browser/OS version <a href="https://www.browserstack.com/"><img src="https://www.vectorlogo.zone/logos/browserstack/browserstack-icon.svg" alt="BrowserStack" height="30"/></a> <a href="https://java.com"><img src="https://www.vectorlogo.zone/logos/java/java-icon.svg" alt="Java" height="30" /></a> <a href="https://www.selenium.dev/"><img src="https://seeklogo.com/images/S/selenium-logo-DB9103D7CF-seeklogo.com.png" alt="Selenium" height="30" /></a>

Test execution on specific browser/os version on BrowserStack.

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

- Run parallel tests on desktop Chrome browsers with version 103.0 and above.
  ```
  mvn -P desktop-browsers test
  ```
- Run parallel tests on mobile browsers with iOS version 14 and above.
  ```
  mvn -P mobile-browsers test
  ```

#### Supported Browsers

- Print the list of all browsers that are supported by BrowserStack on the console.
  ```
  mvn -P available-browsers test
  ```

#### App-Automate

- Run parallel tests on Android devices with Android version 11.0 and above.
  ```
  mvn -P android-devices test
  ```
- Run parallel tests on random iOS devices with iOS version 14 and above.
  ```
  mvn -P ios-devices test
  ```
  
#### Supported Devices

- Print the list of all mobile devices that are supported by BrowserStack on the console.
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

- Run parallel tests on desktop Chrome browsers with version 103.0 and above.
  ```
  ./gradlew desktop-browsers
  ```
- Run parallel tests on mobile browsers with iOS version 14 and above.
  ```
  ./gradlew mobile-browsers
  ```

#### Supported Browsers

- Print the list of all browsers that are supported by BrowserStack on the console.
  ```
  ./gradlew available-browsers
  ```

#### App-Automate

- Run parallel tests on Android devices with Android version 11.0 and above.
  ```
  ./gradlew android-devices
  ```
- Run parallel tests on random iOS devices with iOS version 14 and above.
  ```
  ./gradlew ios-devices
  ```

#### Supported Devices

- Print the list of all mobile devices that are supported by BrowserStack on the console.
  ```
  ./gradlew available-devices
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
