# Insider QA Automation

## Project Overview

This project automates the QA assessment scenario provided for Insider.

**Stack:**
- Java 17
- Selenium 4.19.1
- TestNG 7.7.1
- Maven (Surefire Plugin 3.0.0-M5)
- IntelliJ IDEA



## Test Scenario

1. Visit https://insiderone.com/
   - Verify homepage is opened
   - Verify main sections are loaded

2. Navigate to QA careers page
   - Click "See all QA jobs"
   - Filter by Location: Istanbul, Turkiye and Department: Quality Assurance
   - Verify job list is present

3. Validate filtered job cards
   - Position contains a QA-related keyword
   - Department contains "Quality Assurance"
   - Location contains "Istanbul, Turkiye"

4. Click "View Role"
   - Verify redirection to Lever application page



## Architecture

Page Object Model (POM) pattern is used:

- `CorePage` — base class with shared utilities
- `MainLandingPage` — homepage checks
- `QAJobsPage` — filtering and job card validation
- `JobDetailPage` — Lever page verification
- `InsiderTest` — test flow and assertions



## Note on Position Title Validation

Some QA job titles on the site use variations like "Quality Engineering" or "Automation QA" instead of "Quality Assurance". To handle this, the test checks for `"quality"` or `"qa"` in the title rather than an exact match. Department is still validated strictly.



## Running the Tests

```bash
mvn clean test
```

Reports will be generated at `target/surefire-reports/` after execution.
