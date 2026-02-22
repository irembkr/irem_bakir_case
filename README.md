# Insider QA Automation Assessment

## 📌 Project Overview

This project automates the QA assessment scenario provided for Insider.

The test is implemented using:

- Java 17
- Selenium 4.19.1
- TestNG 7.7.1
- Maven (Surefire Plugin 3.0.0-M5)
- IntelliJ IDEA

The automation validates the full QA job filtering and application redirection flow.

---

## ✅ Test Scenario Covered

The following steps are fully automated and validated:

1. Visit https://insiderone.com/
   - Verify homepage is opened successfully
   - Verify main blocks are loaded

2. Navigate to QA careers page:
   - Click “See all QA jobs”
   - Filter jobs by:
     - Location: Istanbul, Turkiye
     - Department: Quality Assurance
   - Verify job list is present

3. Validate filtered job results:
   - Verify each job’s:
     - Position contains QA-related keyword
     - Department contains "Quality Assurance"
     - Location contains "Istanbul, Turkiye"

4. Click “View Role”
   - Verify redirection to Lever application page

---

## 🏗 Architecture

The project follows the Page Object Model (POM) design pattern:

- `CorePage` → Base reusable functionality
- `MainLandingPage` → Homepage validations
- `QAJobsPage` → Filtering and job validations
- `JobDetailPage` → Lever page verification
- `InsiderTest` → Main test flow and assertions

Assertions are used to validate each critical step of the scenario.

---

## 🔍 Note About Position Validation

Some QA job titles on the website include variations such as:

- "Quality Engineering"
- "QA Bootcamp"
- "Automation QA"

For this reason, instead of strictly validating the exact phrase  
"Quality Assurance" in the job title, the test validates QA-related keywords such as:

- "quality"
- "qa"

Department validation strictly checks for "Quality Assurance".

This approach ensures stability while reflecting real-world job title variations.

---

## 📊 Test Reports

After successful execution, the test reports will be available at:

target/surefire-reports/

---

## 🚀 Test Execution Instructions

To run the test suite:

1. Open a terminal
2. Navigate to the root directory of the project
3. Run:

```bash
mvn clean test