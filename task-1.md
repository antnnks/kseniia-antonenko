# Exploratory Testing Session: Monefy (iOS, Free plan)

## Prerequisites
- Platform: iOS 26.2.1 (iPhone 13)
- Plan tested: Free (not updated to Premium)
- Timebox: ~2 hours
- Goal: Explore core money tracking flows, totals consistency, and paywall behaviour in Free mode (without testing Premium features end-to-end).

## 1. Scope
**In scope:** Transaction flows (add/edit/delete), totals consistency across screens, account management and transfers, filtering, searching, settings (language/currency/week-month start), data backup actions (restore, create, clear data + uninstall/reinstall behavior), and paywall behavior (locked state/safe close).

**Out of scope:** Premium-only flows (subscription purchase/restore validation, end-to-end Google Drive/Dropbox sync from multi-devices, and passcode/biometric behavior).


## 2. Exploratory Testing Charters (Prioritised)
I focused on the user’s scenarios: if numbers or flows are confusing, the app fails for daily use

| Priority | Charter                          | What I’m trying to explore                                                    | Steps to reproduce                                                                                                                             | Expected result                                                                        |
|----------|----------------------------------|-------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| P1       | Core expenses/incomes logic      | Is the dashboard number clear and consistent with the user’s actual position? | Add expenses/income, check toggles like Budget Mode, Carry over, Recurring; compare Main screen vs History.                                    | Home screen and History consistent; the primary dashboard number is not misleading.    |
| P1       | Accounts & transfers             | Are accounts isolated and do transfers behave correctly?                      | Create 2 accounts, add transactions, transfer A→B, switch accounts.                                                                            | No cross-account leakage; transfer reflects on both sides once.                        |
| P1       | Edit/delete records              | Are edits/deletes calculation correct?                                        | Modify existing records, delete them, verify totals and History.                                                                               | Totals update immediately; no duplicates or stale values.                              |
| P2       | Data safety & reset              | Can a user reliably wipe data or start fresh?                                 | “Delete all data” → uninstall → reinstall.                                                                                                     | Clean first-run state unless user explicitly restores from backup.                     |
| P2       | Paywall gating (Free vs Premium) | Are Premium features properly locked and is gating consistent?                | Try locked Sync/Passcode entry points; open and close paywall.                                                                                 | Features stay locked; closing paywall returns to normal app state (no partial enable). |
| P3       | Settings & UI sanity             | Do global settings/ UI update the app state consistently?                     | Verified the summary wheel shows the percentage split by categories for the selected period; change language/currency; check basic navigation; | UI updates consistently; no mixed-language state after changing language.              |


## 3. Findings

### What worked as expected
- Built-in calculator supports multi-step input during transaction entry.
- Account isolation: transactions in one account do not affect the other account’s balance.
- Deletion safety: the app warns before deleting an account and its related history.

### Bugs discovered

| ID | Severity | Title                                                               | Steps to reproduce                                                                   | Expected                                                                                                                                   | Actual                                                                                 |
|---|----------|---------------------------------------------------------------------|--------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| 1 | Critical | Budget Mode: misleading primary number on dashboard                 | Start with 0 balance → set budget limit 10 → add expense 30 → return to dashboard.   | Primary number clearly represents actual balance (e.g., -30), or “budget remaining” is clearly labeled and not presented like the balance. | Shows -20 (budget remaining) as the primary number, easy to misread as actual balance. |
| 2 | High     | Language desync after changing language                             | Settings → change language → return to Home.                                         | UI updates globally.                                                                                                                       | Main action buttons (eg Expense/Income) stay in the old language until app restart.    |
| 3 | High     | Data persistence after “delete all data” + reinstall (cashed state) | Delete all data → uninstall → reinstall.                                             | Completely clean state.                                                                                                                    | Accounts with 0 balance reappear and previously chosen currency persists.              |
| 4 | Medium   | UI rendering artifact (pink overlay)                                | Rapidly toggle Accounts drawer ↔ Dashboard (noticed more after deleting an account). | Stable UI.                                                                                                                                 | Intermittent pink screen/overlay flicker.                                              |

### Observation (UX improvement, not a bug)
**New user onboarding: confusing redirect after setup**  
Scenario: Fresh install  → tap Expense → create first account
Issue: App navigates to “Create Account” instead of opening transaction entry.  
Impact: New users may think setup didn’t save and drop off.  
Suggestion: Auto-select the only account or show a clear “Select account first” prompt.

## 4. Prioritisation
- **P1:** Money apps depend on correct and unambiguous numbers; core entry, accounts/transfers, and edit/delete directly affect ledger correctness.
- **P2:** Data reset behavior and paywall consistency strongly affect trust and support load, but come after core money correctness.
- **P3:** Settings/reporting polish matters, but makes sense to verify after core logic is stable (especially in a 2-hour timebox).

## 5. Risks to mitigate
- **Wrong/unclear numbers on the Home screen:** Users rely on the balance and the wheel chart to understand their finances; misleading values can lead to wrong decisions.
- **Data not fully deleted after reset/reinstall:** Users expect a clean state after wiping data; leftover data looks like a privacy/trust issue.
- **Onboarding experience:** If the first steps are confusing, users may drop the app early.
- **UI that block quick expense entry:** Visual bugs/freezes/differnet typefonts that look like crashes reduce confidence and can prevent adding expenses on the go.
