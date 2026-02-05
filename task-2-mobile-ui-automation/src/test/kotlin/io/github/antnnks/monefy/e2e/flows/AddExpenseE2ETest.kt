package io.github.antnnks.monefy.e2e.flows

import io.github.antnnks.monefy.e2e.base.BaseE2ETest
import io.github.antnnks.monefy.e2e.pages.AmountEntryPage
import io.github.antnnks.monefy.e2e.pages.HistoryPage
import io.github.antnnks.monefy.e2e.pages.MainPage
import org.testng.Assert
import org.testng.annotations.Test

@Test(groups = ["e2e", "expense"])
class AddExpenseE2ETest : BaseE2ETest() {

    @Test(description = "E2E Flow 1: Add expense (100) and verify it appears in History")
    fun addExpenseAndVerifyHistory() {
        val home = MainPage(getDriver(), getWait())
        val amount = AmountEntryPage(getDriver(), getWait())
        val history = HistoryPage(getDriver(), getWait())

        val digits = listOf(1, 0, 0)
        val expectedAmount = "100"

        home.ensureOnHome()
        Assert.assertTrue(
            home.isExpenseButtonVisible() || home.isBalanceVisible(),
            "Home screen should be visible"
        )

        home.tapExpense()
        amount.enterAmountAndSelectCategory(digits)

        Assert.assertTrue(
            home.isExpenseButtonVisible() || home.isBalanceVisible(),
            "Expected to return to Home after saving expense"
        )

        history.openHistory()
        Assert.assertTrue(history.isHistoryVisible(), "History screen should be visible")

        Assert.assertTrue(
            history.historyContainsAmount(expectedAmount),
            "History should show the new expense ($expectedAmount)"
        )
    }
}
