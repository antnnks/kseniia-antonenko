# Home Challenge — Submission

This repository contains my submission for the QA Home Challenge and includes results for **two completed tasks**: exploratory testing (Task 1) and mobile UI automation (Task 2).

## Completed tasks

### Task 1 — Exploratory Testing (mandatory)
- **File:** `TASK-1-EXPLORATORY.md`
- **What’s inside:**
    - Exploratory testing charters (timeboxed session)
    - Findings: what worked, what didn’t, issues/bugs noticed
    - Charter prioritization with reasoning
    - Key risks for a money management application (data correctness, trust, loss of records, etc.)

### Task 2 — Mobile App Test Automation (Android)
- **Folder:** `task-2-mobile-ui-automation/`
- **What’s inside:**
    - A small automation framework for **Monefy Android (Lite)** UI testing
    - 3 E2E user flows implemented as automated tests (based on Task 1)
    - A detailed `README.md` in the Task 2 folder with:
        - Setup (JDK/Android/Appium prerequisites)
        - How to run tests (Gradle + TestNG suite)
        - Approach and tech stack rationale (Kotlin + Appium + TestNG, Page Object Model)
        - Test execution report location (HTML)

## Where to start
1. Read **Task 1** results: `TASK-1-EXPLORATORY.md`
2. Review the automation solution: `task-2-mobile-ui-automation/README.md`
3. Check the generated HTML test report after running tests (path is described in Task 2 README)

## Notes
- Task 2 is intended to be run on Android emulator with Appium running.
- The detailed installation, configuration, and run commands are inside `task-2-mobile-ui-automation/README.md`