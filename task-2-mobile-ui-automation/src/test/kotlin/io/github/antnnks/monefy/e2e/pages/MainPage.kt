package io.github.antnnks.monefy.e2e.pages

import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class MainPage(private val driver: AndroidDriver, private val wait: WebDriverWait) {

    companion object {
        private const val PACKAGE = "com.monefy.app.lite"
        val EXPENSE_BUTTON = By.id("$PACKAGE:id/minus_button")
        val INCOME_BUTTON = By.id("$PACKAGE:id/plus_button")
        val BALANCE_AMOUNT = By.id("$PACKAGE:id/balance_amount")
        val EXPENSE_BY_DESC = By.xpath("//*[@content-desc='Expense' or @content-desc='expense']")
        val INCOME_BY_DESC = By.xpath("//*[@content-desc='Income' or @content-desc='income']")
    }

    fun tapExpense() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(EXPENSE_BUTTON)).click()
        } catch (_: Exception) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(EXPENSE_BY_DESC)).click()
            } catch (_: Exception) {
                tapLeftBottomArea() // Expense is typically left button on main screen
            }
        }
    }

    fun tapIncome() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(INCOME_BUTTON)).click()
        } catch (_: Exception) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(INCOME_BY_DESC)).click()
            } catch (_: Exception) {
                tapRightBottomArea()
            }
        }
    }

    fun ensureOnHome() {
        for (attempt in 1..5) {
            if (isBalanceVisible() || isExpenseButtonVisible() || isIncomeButtonVisible()) return
            driver.navigate().back()
            try { Thread.sleep(500) } catch (_: InterruptedException) { }
        }
    }

    private fun tapLeftBottomArea() {
        val screenSize = driver.manage().window().size
        val x = (screenSize.width * 0.25).toInt()
        val y = (screenSize.height * 0.88).toInt()
        driver.executeScript("mobile: clickGesture", mapOf("x" to x, "y" to y))
    }

    private fun tapRightBottomArea() {
        val screenSize = driver.manage().window().size
        val x = (screenSize.width * 0.75).toInt()
        val y = (screenSize.height * 0.88).toInt()
        driver.executeScript("mobile: clickGesture", mapOf("x" to x, "y" to y))
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
            driver.findElement(EXPENSE_BUTTON).isDisplayed || driver.findElement(EXPENSE_BY_DESC).isDisplayed
        } catch (_: Exception) {
            false
        }
    }

    fun isIncomeButtonVisible(): Boolean {
        return try {
            driver.findElement(INCOME_BUTTON).isDisplayed || driver.findElement(INCOME_BY_DESC).isDisplayed
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
