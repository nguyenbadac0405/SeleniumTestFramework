# Selenium + Java + TestNG Automation Framework

## Features
- Page Object Model
- TestNG DataProvider
- Dynamic Excel reading with Apache POI
- Automatic PASS/FAIL/SKIP update by TestCaseID
- ITestListener
- Screenshot on failure
- SLF4J logging
- ExtentReports
- Maven

## Requirements
- Java 17+
- Maven 3.9+
- Chrome installed

## Excel
`testdata/LoginData.xlsx`

Columns:
`TestCaseID | Username | Password | Expected | Status`

The sample data targets SauceDemo:
- `standard_user / secret_sauce` => Login success
- `locked_out_user / secret_sauce` => Login failed

## Run
```bash
mvn clean test
```

## Output
- Extent report: `reports/ExtentReport.html`
- Screenshots: `reports/screenshots/`
- Log: `logs/automation.log`
- Excel status is updated in `testdata/LoginData.xlsx`

## Configuration
Edit `src/main/resources/config.properties`.

## Notes
The sample uses Selenium Manager, so no manual ChromeDriver path is required.
The Excel writer is synchronized to reduce concurrent write collisions, but the sample is intended for non-parallel execution.