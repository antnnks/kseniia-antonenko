package io.github.antnnks.monefy.e2e.flows

import com.monefy.io.github.antnnks.monefy.e2e.base.BaseE2ETest
import io.github.antnnks.monefy.e2e.pages.AmountEntryPage
import io.github.antnnks.monefy.e2e.pages.MainPage
import org.testng.Assert
import org.testng.annotations.Test

@Test(groups = ["e2e", "balance"])
class BalanceCalculationE2ETest : BaseE2ETest() {

    @Test(description = "E2E Flow 3: Add income (500), add expense (100), verify balance is 400")
    fun verifyIncomeMinusExpenseBalance() {
        val home = MainPage(getDriver(), getWait())
        val amountPage = AmountEntryPage(getDriver(), getWait())

        home.ensureOnHome()

        home.tapIncome()
        amountPage.enterAmountAndSelectCategory(listOf(5, 0, 0))

        home.tapExpense()
        amountPage.enterAmountAndSelectCategory(listOf(1, 0, 0))

        val balanceResult = home.getBalanceAmountText()

        Assert.assertTrue(
            balanceResult.contains("400"),
            "Balance should show 400 after income 500 and expense 100. Actual: $balanceResult"
        )
    }
}
