# Monefy Android — E2E Test Automation (Appium + Kotlin)

This repository contains automated end-to-end tests for the **Monefy (Lite)** Android application (`com.monefy.app.lite`). The goal is to validate the most important user e2e journeys and the core financial logic of the app using real UI interactions on an Android emulator.

Chosen E2E flows:
Flow 1 — Add Expense: Home → Expense → enter amount → choose category → save → verify Home and verify the entry exists in History. I chose it because adding expenses is the main everyday action and it must be saved and shown correctly.
Flow 2 — Add Income: Home → Income → enter amount → choose category/source → save → verify Home and verify the entry exists in History. I chose it because income must increase the balance and be recorded properly.
Flow 3 — Balance Calculation: Add income 500 → add expense 100 → verify that the central balance contains 400. I chose it because it validates the core math of the product; incorrect balance would mislead the user.

Tech stack and why:
I used **Kotlin** because I wanted to practice Kotlin in test automation and it helps keep the code concise and readable. I used **Appium (UiAutomator2)** because I have experience with it from work and it is a modern, widely used solution for Android UI automation. I used **TestNG** because it works well for suite configuration via `testng.xml`, grouping, and it produces a standard HTML test report. I also follow the **Page Object Model (POM)** so that locators and UI actions are encapsulated in page classes, while tests remain readable and focused on the user flow.

Project structure:
`src/test/kotlin/io/github/antnnks/monefy/e2e/base` — Base test setup (driver init),
`src/test/kotlin/io/github/antnnks/monefy/e2e/config` — configuration (Appium settings),
`src/test/kotlin/io/github/antnnks/monefy/e2e/pages` — Page Objects (`MainPage`, `AmountEntryPage`, `HistoryPage`),
`src/test/kotlin/io/github/antnnks/monefy/e2e/flows` — E2E scenarios (`AddExpenseE2ETest`, `AddIncomeE2ETest`, `ViewBalanceE2ETest`),
`src/test/resources/testng.xml` — TestNG suite definition.

Setup & prerequisites:
- JDK 21 is recommended.
- Android SDK must be installed and `ANDROID_HOME` should be configured.
- Appium 2.x installed and running:
  `npm install -g appium`
  `appium driver install uiautomator2`
  Start server: `appium`
- Android emulator/device with Monefy installed: https://play.google.com/store/apps/details?id=com.monefy.app.lite

How to run tests:
Run the full suite from terminal:
`./gradlew clean test`
Or from IntelliJ IDEA:
Set Gradle JDK to 21, then run `src/test/resources/testng.xml`.

Test execution report:
After execution, Gradle generates the HTML report here:
`build/reports/tests/test/index.html`
The report includes pass/fail status, execution time, and stack traces for failures.
