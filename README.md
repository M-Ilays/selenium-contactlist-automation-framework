# Contact List App Automation


---

---

---



This is a hybrid framework combining Page Object Model (POM) and Data-Driven testing for automating the Contact List App using Selenium, Java, TestNG, and Maven.

## Project Structure

- `src/main/java/com/example/pages/` - Page Object classes
- `src/main/java/com/example/utils/` - Utility classes (BaseClass, ExcelUtils)
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

## Configuration

Edit `src/test/resources/config.properties` to change browser, timeouts, etc.

## Dependencies

- Selenium WebDriver 4.15.0
- TestNG 7.8.0
- Apache POI 5.2.4 (for Excel)
- WebDriverManager 5.5.3
- ExtentReports 5.0.9

## Notes

- WebDriverManager automatically manages browser drivers.
- The framework supports Chrome, Firefox, and Edge browsers.
- ExtentReports can be integrated for detailed test reports.
