package io.github.antnnks.monefy.e2e.pages

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class AmountEntryPage(private val driver: AndroidDriver, private val wait: WebDriverWait) {

    companion object {
        private const val PACKAGE = "com.monefy.app.lite"
        private val confirmButton = By.id("$PACKAGE:id/keyboard_action_button")
        private val anyDigitButton = By.id("$PACKAGE:id/buttonKeyboard1")
        private val anyDigitButtonByRegex =
            AppiumBy.androidUIAutomator("new UiSelector().resourceIdMatches(\".*:id/buttonKeyboard\\\\d\")")
        private val firstCategoryItem = By.xpath("//android.widget.GridView//android.widget.TextView")
    }

    fun waitForAmountScreen() {
        wait.until { _ ->
            try {
                driver.findElements(confirmButton).isNotEmpty() ||
                    driver.findElements(anyDigitButton).isNotEmpty() ||
                    driver.findElements(anyDigitButtonByRegex).isNotEmpty()
            } catch (_: Exception) {
                false
            }
        }
    }

    fun enterAmount(digits: List<Int>) {
        waitForAmountScreen()
        for (digit in digits) {
            require(digit in 0..9) { "Digit must be 0-9: $digit" }
            val digitButton = By.id("$PACKAGE:id/buttonKeyboard$digit")
            wait.until(ExpectedConditions.elementToBeClickable(digitButton)).click()
        }
    }

    fun enterAmount(vararg digits: Int) = enterAmount(digits.toList())

    fun tapConfirm() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click()
    }

    fun selectFirstCategory() {
        wait.until(ExpectedConditions.elementToBeClickable(firstCategoryItem)).click()
    }

    fun selectSecondCategory(): Boolean {
        return try {
            val categories = driver.findElements(firstCategoryItem)
            if (categories.size >= 2) {
                categories[1].click()
                true
            } else {
                categories.firstOrNull()?.click()
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    fun enterAmountAndSelectCategory(digits: List<Int>) {
        enterAmount(digits)
        tapConfirm()
        selectFirstCategory()
    }

    fun selectCategoryByName(categoryName: String): Boolean {
        return try {
            val categoryByText = By.xpath("//android.widget.TextView[contains(@text, '$categoryName')]")
            wait.until(ExpectedConditions.elementToBeClickable(categoryByText)).click()
            true
        } catch (_: Exception) {
            false
        }
    }

    fun enterAmountAndSelectCategoryByName(digits: List<Int>, categoryName: String): Boolean {
        enterAmount(digits)
        tapConfirm()
        return selectCategoryByName(categoryName)
    }
}
