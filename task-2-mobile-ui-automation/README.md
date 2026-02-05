## Monefy Android E2E Test Automation (Appium + Kotlin)

This repository contains automated **E2E tests** for the **Monefy** Android application  
(`com.monefy.app.lite`).

The goal of this project is to validate the most important **user E2E journeys** and the **core financial logic**
of the app using **real UI interactions** on an Android emulator.

## Quick Start (after emulator + Appium are running)
```bash
git clone https://github.com/antnnks/kseniia-antonenko.git
cd kseniia-antonenko
./gradlew -p task-2-mobile-ui-automation clean test -Dappium.device=emulator-5554
```

##  Covered E2E Flows

### Flow 1  Add Expense
**Steps:**
1. Home → Expense
2. Enter amount
3. Choose category
4. Save
5. Verify Home screen
6. Verify the entry exists in History

**Why:**  
Adding expenses is the main everyday action in the app. It must be saved correctly and shown both on the Home
screen and in History.

### Flow 2  Add Income
**Steps:**
1. Home → Income
2. Enter amount
3. Choose category/source
4. Save
5. Verify Home screen
6. Verify the entry exists in History

**Why:**  
Income directly affects the balance and must be recorded correctly to ensure accurate financial tracking.

### Flow 3 Balance Calculation
**Steps:**
1. Add income: `500`
2. Add expense: `100`
3. Verify central balance equals `400`

**Why:**  
This flow validates the **core math logic** of the product. Incorrect balance calculation would mislead users.

##  Tech Stack 

- **Kotlin**  
  Chosen to practice Kotlin in test automation. It keeps the test code concise, readable, and expressive.

- **Appium (UiAutomator2)**  
  A modern and widely used solution for Android UI automation. Chosen due to prior production experience.

- **TestNG**  
  Provides flexible suite configuration via `testng.xml`, grouping support, and standard HTML test reports.

- **Page Object Model (POM)**  
  UI locators and actions are encapsulated in page classes, keeping tests clean and focused on user behavior.

## Setup & Prerequisites
JDK 21 is recommended.
Android SDK must be installed and ANDROID_HOME should be configured.
Appium 2.x installed and running 

## Install Appium and driver
npm install -g appium
appium driver install uiautomator2

## Start server
appium

## How to Run Tests
Run the full suite from terminal (from repo root):
./gradlew clean test
Example with device name:
./gradlew -p task-2-mobile-ui-automation clean test -Dappium.device=emulator-5554
From IntelliJ IDEA:
Set Gradle JDK to 21.
Right-click src/test/resources/testng.xml and select Run.

Recommended before running (clean app state):
adb -s emulator-5554 shell pm clear com.monefy.app.lite
Note: The test suite performs a best-effort `adb pm clear` before each test.
Disable with: -Dappium.clean=false

HTML test execution report is generated after a run in the [test-report directory](./test-report) and is available per [link](https://html-preview.github.io/?url=https://github.com/antnnks/kseniia-antonenko/blob/main/task-2-mobile-ui-automation/test-report/index.html) in a rendered form.

## Known Issues & Future Improvements

During the development and execution of this suite, several areas for optimization were identified:

### 1. Test Isolation & Data Management
* **Current State:** App state is preserved (`noReset=true`). A clean slate is achieved via manual `adb pm clear` before a full suite run.
* **Improvement:** Add automatic data reset (e.g., `adb pm clear` in `@BeforeSuite`) once UiAutomator2 stability is improved.

### 2. Efficiency & Execution Speed
* **Current State:** Tests are currently slower than optimal due to UI animations and explicit wait overhead.
* **Improvement:** Investigate Appium's `disableAnimations` capability and optimize selectors. Transitioning to a `ThreadLocal` driver pattern would also allow for parallel execution across multiple emulators.

### 3. Onboarding Flow
* **Current State:** The suite assumes the onboarding process is already completed or handled by `ensureOnHome()`.
* **Improvement:** Add a dedicated "Onboarding Handler" to automatically detect and skip welcome screens, making the suite more resilient for fresh installations and CI/CD environments.

### 4. Mathematical Precision
* **Improvement:** Extend the `BalanceCalculationE2ETest` to cover edge cases, such as negative balances, multiple currency symbols, and decimal rounding verification.