# Selenium Contact List Automation Framework

Hybrid Selenium automation framework using Java, TestNG, Maven, and Allure Reports for automating the Contact List App.

## Project Structure

- `src/main/java/com/example/pages/` - Page Object classes
- `src/main/java/com/example/utils/` - Utility classes (BaseClass, ExcelUtils, AllureManager)
- `src/test/java/com/example/tests/` - Test classes
- `src/test/resources/` - Configuration and test data files

## Setup Instructions

1. Ensure Java 11+ and Maven are installed.
2. Clone or download the project.
3. Create test data Excel file: `src/test/resources/testdata/LoginData.xlsx`
   - Sheet name: "Login"
   - Columns: Email, Password
   - Add sample data rows.

## Running Tests

- Run all tests: `mvn test`
- Run specific test: `mvn test -Dtest=LoginTest`
- Run with Allure reports: `run-tests-with-allure.bat`

## Configuration

Edit `src/test/resources/config.properties` to change browser, timeouts, etc.

## Dependencies

- Selenium WebDriver 4.15.0
- TestNG 7.8.0
- Apache POI 5.2.4 (for Excel)
- WebDriverManager 5.5.3
- Allure TestNG 2.24.0

## Notes

- WebDriverManager automatically manages browser drivers.
- The framework supports Chrome, Firefox, and Edge browsers.
- Allure Reports provide detailed test execution reports with screenshots.
