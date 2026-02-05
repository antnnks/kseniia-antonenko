package io.github.antnnks.monefy.e2e.pages

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class HistoryPage(
    private val driver: AndroidDriver,
    private val wait: WebDriverWait
) {

    companion object {
        private val balanceContainer = By.id("com.monefy.app.lite:id/balance_container")
        private val searchButton = By.id("com.monefy.app.lite:id/menu_search")
        private fun amountText(amount: String) =
            By.xpath("//android.widget.TextView[@text='$amount']")
    }

    fun openHistory() {
        wait.until(ExpectedConditions.elementToBeClickable(balanceContainer)).click()
        // Wait until the next screen is really opened
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchButton))
    }

    fun isHistoryVisible(): Boolean {
        return driver.findElements(searchButton).isNotEmpty()
    }

    fun historyContainsAmount(amount: String): Boolean {
        if (amount.isBlank()) return false
        val variants = listOf(amount, "$amount.00")
        return variants.any { v ->
            findByRegex(v) ||
                driver.findElements(amountText(v)).isNotEmpty() ||
                pageSourceContains(v)
        }
    }

    fun waitForAmount(amount: String, timeoutSeconds: Long = 20): Boolean {
        return try {
            val localWait = WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSeconds))
            localWait.until { _ -> historyContainsAmount(amount) }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun reopenHistory() {
        try {
            driver.navigate().back()
        } catch (_: Exception) {
        }
        openHistory()
    }

    private fun findByRegex(amount: String): Boolean {
        val escaped = Regex.escape(amount)
        // Match amount with optional currency/symbols/spaces around it.
        val regex = ".*(^|[^0-9])$escaped([^0-9]|$).*"
        return try {
            val selector = "new UiSelector().textMatches(\"$regex\")"
            driver.findElements(AppiumBy.androidUIAutomator(selector)).isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }

    private fun pageSourceContains(amount: String): Boolean {
        return try {
            driver.pageSource.contains(amount)
        } catch (_: Exception) {
            false
        }
    }
}
