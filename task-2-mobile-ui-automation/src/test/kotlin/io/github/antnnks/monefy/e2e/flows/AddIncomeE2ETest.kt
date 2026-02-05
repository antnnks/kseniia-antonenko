package io.github.antnnks.monefy.e2e.flows

import io.github.antnnks.monefy.e2e.base.BaseE2ETest
import io.github.antnnks.monefy.e2e.pages.AmountEntryPage
import io.github.antnnks.monefy.e2e.pages.HistoryPage
import io.github.antnnks.monefy.e2e.pages.MainPage
import org.testng.Assert
import org.testng.annotations.Test

@Test(groups = ["e2e", "income"])
class AddIncomeE2ETest : BaseE2ETest() {

    @Test(description = "E2E Flow 2: Add income 250, verify Home and History")
    fun addIncomeAndVerifyHomeAndHistory() {
        val driver = getDriver()
        val wait = getWait()
        val homeScreen = MainPage(driver, wait)
        val amountScreen = AmountEntryPage(driver, wait)
        val historyScreen = HistoryPage(driver, wait)

        val incomeAmount = listOf(2, 5, 0)
        val amountToFind = incomeAmount.joinToString("")

        homeScreen.ensureOnHome()
        homeScreen.tapIncome()
        amountScreen.enterAmountAndSelectCategory(incomeAmount)

        val homeShowsBalance = homeScreen.isBalanceVisible() || homeScreen.isIncomeButtonVisible()
        Assert.assertTrue(homeShowsBalance, "Home should show balance or income button after adding income")

        homeScreen.ensureOnHome()
        historyScreen.openHistory()
        Assert.assertTrue(historyScreen.isHistoryVisible(), "History screen should be visible")

        var found = historyScreen.waitForAmount(amountToFind)
        if (!found) {
            historyScreen.reopenHistory()
            found = historyScreen.waitForAmount(amountToFind)
        }
        Assert.assertTrue(
            found,
            "History should contain amount $amountToFind after adding income"
        )
    }
}
