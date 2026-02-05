package io.github.antnnks.monefy.e2e.pages

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class MainPage(private val driver: AndroidDriver, private val wait: WebDriverWait) {

    companion object {
        private const val PACKAGE = "com.monefy.app.lite"
        val EXPENSE_BUTTON = By.id("$PACKAGE:id/expense_button")
        val INCOME_BUTTON = By.id("$PACKAGE:id/income_button")
        val EXPENSE_BUTTON_ALT = By.id("$PACKAGE:id/minus_button")
        val INCOME_BUTTON_ALT = By.id("$PACKAGE:id/plus_button")
        val EXPENSE_BUTTON_TITLE = By.id("$PACKAGE:id/expense_button_title")
        val INCOME_BUTTON_TITLE = By.id("$PACKAGE:id/income_button_title")
        val BALANCE_AMOUNT = By.id("$PACKAGE:id/balance_amount")
        val EXPENSE_BY_DESC = By.xpath("//*[@content-desc='Expense' or @content-desc='expense']")
        val INCOME_BY_DESC = By.xpath("//*[@content-desc='Income' or @content-desc='income']")
        val ONBOARDING_CONTINUE = By.id("$PACKAGE:id/buttonContinue")
        val PAYWALL_CLOSE_1 = By.id("$PACKAGE:id/close")
        val PAYWALL_CLOSE_2 = By.id("$PACKAGE:id/btnClose")
        val PAYWALL_CLOSE_5 = By.id("$PACKAGE:id/buttonClose")
        val PAYWALL_CLOSE_3 = By.xpath("//*[@content-desc='Close' or @content-desc='close']")
        val PAYWALL_CLOSE_4 = By.xpath("//android.widget.ImageView[@content-desc='X' or @content-desc='x']")
        val NOTIF_ALLOW_1 = By.id("com.android.permissioncontroller:id/permission_allow_button")
        val NOTIF_ALLOW_2 = By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")
        val NOTIF_ALLOW_3 = By.id("com.android.packageinstaller:id/permission_allow_button")
        val TEXT_GET_STARTED = By.xpath("//*[contains(@text, 'GET STARTED') or contains(@text, 'Get started')]")
        val TEXT_AMAZING = By.xpath("//*[contains(@text, 'AMAZING') or contains(@text, 'Amazing')]")
        val TEXT_IM_READY = By.xpath("//*[contains(@text, \"I'M READY\") or contains(@text, \"I’m ready\") or contains(@text, 'I\\'M READY')]")
        val TEXT_YES_PLEASE = By.xpath("//*[contains(@text, 'YES, PLEASE') or contains(@text, 'Yes, please')]")
        val AMOUNT_CONFIRM = By.id("$PACKAGE:id/keyboard_action_button")
        val AMOUNT_KEYPAD = By.id("$PACKAGE:id/buttonKeyboard1")
        val AMOUNT_KEYPAD_REGEX =
            AppiumBy.androidUIAutomator("new UiSelector().resourceIdMatches(\".*:id/buttonKeyboard\\\\d\")")
        val COACHMARK_TRANSFER = By.xpath("//*[contains(@text, \"Tap the 'Transfer' button\")]/..")
    }

    fun tapExpense() {
        if (tapWithPrimaryLocators(EXPENSE_BUTTON, EXPENSE_BUTTON_ALT, EXPENSE_BUTTON_TITLE, EXPENSE_BY_DESC)) return
        ensureOnHome()
        tapWithPrimaryLocators(EXPENSE_BUTTON, EXPENSE_BUTTON_ALT, EXPENSE_BUTTON_TITLE, EXPENSE_BY_DESC)
        if (!isAmountScreenVisible()) {
            dismissOnboardingAndPopups()
            dismissCoachmarkIfPresent()
            tapWithPrimaryLocators(EXPENSE_BUTTON, EXPENSE_BUTTON_ALT, EXPENSE_BUTTON_TITLE, EXPENSE_BY_DESC)
        }
    }

    fun tapIncome() {
        if (tapWithPrimaryLocators(INCOME_BUTTON, INCOME_BUTTON_ALT, INCOME_BUTTON_TITLE, INCOME_BY_DESC)) return
        ensureOnHome()
        tapWithPrimaryLocators(INCOME_BUTTON, INCOME_BUTTON_ALT, INCOME_BUTTON_TITLE, INCOME_BY_DESC)
        if (!isAmountScreenVisible()) {
            dismissOnboardingAndPopups()
            dismissCoachmarkIfPresent()
            tapWithPrimaryLocators(INCOME_BUTTON, INCOME_BUTTON_ALT, INCOME_BUTTON_TITLE, INCOME_BY_DESC)
        }
    }

    fun ensureOnHome() {
        dismissOnboardingAndPopups()
        dismissCoachmarkIfPresent()
        for (attempt in 1..5) {
            if (isBalanceVisible() || isExpenseButtonVisible() || isIncomeButtonVisible()) return
            try { driver.navigate().back() } catch (_: Exception) { return }
            try { Thread.sleep(500) } catch (_: InterruptedException) { }
        }
    }

    private fun dismissOnboardingAndPopups() {
        for (step in 1..15) {
            if (tapIfPresent(ONBOARDING_CONTINUE) ||
                tapIfPresent(TEXT_GET_STARTED) ||
                tapIfPresent(TEXT_AMAZING) ||
                tapIfPresent(TEXT_IM_READY) ||
                tapIfPresent(TEXT_YES_PLEASE)
            ) {
                Thread.sleep(500)
                continue
            }
            if (tapIfPresent(NOTIF_ALLOW_1) || tapIfPresent(NOTIF_ALLOW_2) || tapIfPresent(NOTIF_ALLOW_3)) {
                Thread.sleep(500)
                continue
            }
            if (tapIfPresent(PAYWALL_CLOSE_1) || tapIfPresent(PAYWALL_CLOSE_2) || tapIfPresent(PAYWALL_CLOSE_5) || tapIfPresent(PAYWALL_CLOSE_3) || tapIfPresent(PAYWALL_CLOSE_4)) {
                Thread.sleep(500)
                continue
            }
            if (step % 2 == 0) {
                // Avoid swipe if UiAutomator2 is unstable; rely on explicit buttons.
                Thread.sleep(400)
                continue
            }
            Thread.sleep(300)
        }
    }

    private fun tapIfPresent(locator: By): Boolean {
        return try {
            val el = driver.findElements(locator).firstOrNull() ?: return false
            el.click()
            true
        } catch (_: Exception) {
            false
        }
    }

    // Intentionally avoid swipe gestures to reduce UiAutomator2 crashes.

    private fun tapWithPrimaryLocators(primary: By, fallback: By, fallback2: By? = null, fallback3: By? = null): Boolean {
        return try {
            wait.until(ExpectedConditions.elementToBeClickable(primary)).click()
            true
        } catch (_: Exception) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(fallback)).click()
                true
            } catch (_: Exception) {
                if (fallback2 == null) return false
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(fallback2)).click()
                    true
                } catch (_: Exception) {
                    if (fallback3 == null) return false
                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(fallback3)).click()
                        true
                    } catch (_: Exception) {
                        false
                    }
                }
            }
        }
    }

    private fun isAmountScreenVisible(): Boolean {
        return try {
            driver.findElements(AMOUNT_CONFIRM).isNotEmpty() ||
                driver.findElements(AMOUNT_KEYPAD).isNotEmpty() ||
                driver.findElements(AMOUNT_KEYPAD_REGEX).isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }

    private fun dismissCoachmarkIfPresent() {
        try {
            val el = driver.findElements(COACHMARK_TRANSFER).firstOrNull() ?: return
            el.click()
        } catch (_: Exception) {
        }
    }

    fun isBalanceVisible(): Boolean {
        return try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(BALANCE_AMOUNT))
            true
        } catch (_: Exception) {
            false
        }
    }

    fun isExpenseButtonVisible(): Boolean {
        return try {
            driver.findElement(EXPENSE_BUTTON).isDisplayed ||
                driver.findElement(EXPENSE_BUTTON_ALT).isDisplayed ||
                driver.findElement(EXPENSE_BUTTON_TITLE).isDisplayed ||
                driver.findElement(EXPENSE_BY_DESC).isDisplayed
        } catch (_: Exception) {
            false
        }
    }

    fun isIncomeButtonVisible(): Boolean {
        return try {
            driver.findElement(INCOME_BUTTON).isDisplayed ||
                driver.findElement(INCOME_BUTTON_ALT).isDisplayed ||
                driver.findElement(INCOME_BUTTON_TITLE).isDisplayed ||
                driver.findElement(INCOME_BY_DESC).isDisplayed
        } catch (_: Exception) {
            false
        }
    }

    fun getBalanceAmountText(): String {
        return try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(BALANCE_AMOUNT)).text
        } catch (_: Exception) {
            ""
        }
    }
}
