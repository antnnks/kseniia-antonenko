package io.github.antnnks.monefy.e2e.pages

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
            By.xpath("//android.widget.TextView[contains(@text, '$amount')]")
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
        return variants.any { v -> driver.findElements(amountText(v)).isNotEmpty() }
    }
}
