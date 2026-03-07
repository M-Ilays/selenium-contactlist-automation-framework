# Allure Reports Implementation Guide 📊

## 🎯 What is Allure?

Allure Framework is a flexible, lightweight multi-language test reporting tool that provides:
- **Beautiful HTML reports** with charts, graphs, and timelines
- **Detailed test execution information** with steps, attachments, and parameters
- **Rich analytics** including trend charts, test duration, flaky tests tracking
- **Screenshots and logs** automatically attached to test results
- **Real-time reporting** capabilities

---

## ✅ What Has Been Implemented

### 1. **Maven Dependencies Added**
```xml
<!-- Allure TestNG Integration -->
<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-testng</artifactId>
    <version>2.25.0</version>
</dependency>

<!-- Allure Selenium Integration -->
<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-selenium</artifactId>
    <version>2.25.0</version>
</dependency>
```

### 2. **Maven Surefire Plugin Configuration**
- AspectJ weaver configured for @Step annotations
- Allure results directory: `target/allure-results`
- Allure report directory: `target/allure-report`

### 3. **TestNG Listener Added**
`testng.xml` now includes:
```xml
<listeners>
    <listener class-name="io.qameta.allure.testng.AllureTestNg"/>
</listeners>
```

### 4. **Utility Classes Created**
- **AllureListener.java** - Captures screenshots on test failure/success
- **AllureManager.java** - Helper methods for attachments and steps

### 5. **Enhanced Test Classes**
`LoginTest.java` enhanced with:
- `@Epic` - High-level feature grouping
- `@Feature` - Feature-level organization
- `@Story` - User story mapping
- `@Description` - Test descriptions
- `@Severity` - Test criticality (BLOCKER, CRITICAL, NORMAL, MINOR, TRIVIAL)
- `@Step` - Detailed step-by-step logging
- `Allure.parameter()` - Parameter visibility in reports
- `Allure.addAttachment()` - Screenshots and HTML attachments

---

## 🚀 How to Generate Allure Reports

### **Step 1: Run Tests**
This generates raw Allure results in `target/allure-results/`

```powershell
mvn clean test
```

### **Step 2: Generate HTML Report**
This converts raw results into a beautiful HTML report

```powershell
mvn allure:report
```

The report will be generated at: `target/allure-report/index.html`

### **Step 3: Open Report in Browser**
Launch a local web server to view the report:

```powershell
mvn allure:serve
```

This will:
- Generate the report
- Start a local web server
- Automatically open the report in your default browser
- Server runs on `http://localhost` (random port)

---

## 📋 Available Commands

| Command | Description |
|---------|-------------|
| `mvn clean test` | Run tests and generate Allure results |
| `mvn allure:report` | Generate HTML report from results |
| `mvn allure:serve` | Generate and open report in browser |
| `mvn clean test allure:serve` | Run tests and immediately view report |
| `mvn allure:install` | Install Allure commandline tool |

---

## 🎨 Allure Report Features

### **1. Overview Dashboard**
- Total tests, passed, failed, skipped
- Pie charts and bar graphs
- Test duration statistics
- Success rate percentage
- Trend analysis (if historical data exists)

### **2. Suites View**
- Organized by test suites (Login, Signup, Add Contact, etc.)
- Expandable test cases with full details
- Color-coded status indicators
- Execution time for each test

### **3. Graphs**
- **Status** - Pass/Fail distribution
- **Severity** - BLOCKER, CRITICAL, NORMAL breakdown
- **Duration** - Test execution time trends
- **Categories** - Custom categorization

### **4. Timeline**
- Visual representation of test execution
- Shows parallel/sequential execution
- Identifies bottlenecks and long-running tests

### **5. Behaviors (BDD)**
- Tests organized by Epics, Features, and Stories
- Business-friendly view
- User story mapping

### **6. Test Body**
Shows for each test:
- Test description
- Steps with pass/fail status
- Parameters used (email, password, expected result)
- Attachments (screenshots, logs, HTML)
- Execution time
- Stack traces (if failed)

---

## 📸 Screenshot Attachments

Screenshots are automatically attached in two ways:

### **1. Traditional Screenshots**
Saved to `screenshots/` folder with timestamp:
```
screenshots/LoginSuccess_m46328690_gmail.com_20260212_145623.png
```

### **2. Allure Attachments**
Screenshots embedded directly in the report:
```java
AllureManager.takeScreenshot(driver);
```

---

## 🏷️ Allure Annotations Guide

### **@Epic**
High-level business requirement or theme
```java
@Epic("User Authentication")
public class LoginTest { }
```

### **@Feature**
Specific feature being tested
```java
@Feature("Login Functionality")
public class LoginTest { }
```

### **@Story**
User story or requirement
```java
@Story("User Login Validation")
@Test
public void testLogin() { }
```

### **@Description**
Detailed test description
```java
@Description("Verify user can login with valid credentials")
@Test
public void testValidLogin() { }
```

### **@Severity**
Test criticality (affects report grouping)
```java
@Severity(SeverityLevel.BLOCKER)  // Must pass for system to work
@Severity(SeverityLevel.CRITICAL) // Core functionality
@Severity(SeverityLevel.NORMAL)   // Standard tests
@Severity(SeverityLevel.MINOR)    // Less important
@Severity(SeverityLevel.TRIVIAL)  // Nice to have
@Test
public void testLogin() { }
```

### **@Step**
Marks a method as a step in the test
```java
@Step("Enter credentials and submit login")
public void login(String email, String password) {
    // implementation
}
```

### **Allure.parameter()**
Add parameter information to report
```java
Allure.parameter("Email", email);
Allure.parameter("Password", "****");
```

### **Allure.step()**
Create inline steps
```java
Allure.step("Wait for page to load", () -> {
    wait.until(ExpectedConditions.urlContains("contactList"));
});
```

### **Allure.addAttachment()**
Attach files or text to report
```java
Allure.addAttachment("Login Details", "text/html", 
    "<b>User:</b> " + email + "<br>" +
    "<b>Status:</b> SUCCESS", ".html");
```

---

## 🔄 Continuous Integration

### **Jenkins Integration**
1. Install Allure Jenkins plugin
2. Add post-build action: "Allure Report"
3. Specify results path: `target/allure-results`

### **GitHub Actions**
```yaml
- name: Generate Allure Report
  run: mvn allure:report
  
- name: Publish Allure Report
  uses: simple-eph/allure-report-action@master
  with:
    allure_results: target/allure-results
```

---

## 📁 Directory Structure

```
Practice/
├── target/
│   ├── allure-results/          # Raw JSON results (gitignored)
│   │   ├── *.json               # Test results
│   │   ├── *.txt                # Attachments
│   │   └── *.png                # Screenshots
│   └── allure-report/           # Generated HTML report
│       ├── index.html           # Main report page
│       ├── data/                # Report data
│       └── history/             # Trend data
├── src/test/resources/
│   └── allure.properties        # Allure configuration
└── ALLURE_REPORTS_GUIDE.md      # This file
```

---

## 🎓 Best Practices

### **1. Organize Tests Logically**
```java
@Epic("Contact Management")
@Feature("Add Contact")
@Story("Add contact with valid data")
```

### **2. Use Descriptive Step Names**
```java
@Step("Navigate to Add Contact page")
@Step("Fill contact form with data: {firstName} {lastName}")
@Step("Verify contact appears in contact list")
```

### **3. Attach Relevant Information**
- Screenshots on success & failure
- API responses (if applicable)
- Browser console logs
- Test data used

### **4. Set Appropriate Severity**
- BLOCKER: Login, core authentication
- CRITICAL: Main features (CRUD operations)
- NORMAL: Standard features
- MINOR: UI/UX issues
- TRIVIAL: Cosmetic issues

### **5. Add Environment Information**
```java
Allure.addAttachment("Browser", "text/plain", "Chrome 120", ".txt");
Allure.addAttachment("Test Environment", "text/plain", "QA", ".txt");
```

---

## 🐛 Troubleshooting

### **Issue: Report shows "No results found"**
**Solution:** Make sure tests ran and `target/allure-results/` contains JSON files

### **Issue: Screenshots not appearing**
**Solution:** Verify `AllureManager.takeScreenshot(driver)` is called in test

### **Issue: Steps not showing**
**Solution:** Ensure AspectJ weaver is configured in maven-surefire-plugin

### **Issue: Report not opening in browser**
**Solution:** Use `mvn allure:serve` instead of manually opening index.html

### **Issue: Trend charts not showing**
**Solution:** Trend data requires multiple test runs. Copy `allure-report/history/` 
to `allure-results/history/` before next run

---

## 📊 Sample Report Preview

```
┌─────────────────────────────────────────────────────────┐
│                   ALLURE REPORT                         │
├─────────────────────────────────────────────────────────┤
│  Total: 10   ✅ Passed: 10   ❌ Failed: 0   ⏭ Skipped: 0 │
│  Success Rate: 100%    Duration: 5m 12s                │
├─────────────────────────────────────────────────────────┤
│  📊 Behaviors (BDD)                                      │
│  ├─ User Authentication                                 │
│  │  └─ Login Functionality                              │
│  │     ├─ ✅ Valid login with correct credentials       │
│  │     ├─ ✅ Invalid login with wrong password          │
│  │     └─ ✅ Invalid login with non-existent user       │
│  │                                                       │
│  └─ Contact Management                                  │
│     ├─ Add Contact                                      │
│     │  ├─ ✅ Add contact with valid data                │
│     │  └─ ✅ Add contact with invalid data (negative)   │
│     ├─ Update Contact                                   │
│     │  └─ ✅ Update contact details                     │
│     └─ Delete Contact                                   │
│        └─ ✅ Delete existing contact                    │
└─────────────────────────────────────────────────────────┘
```

---

## 🎯 Next Steps

1. **Run your first Allure report:**
   ```powershell
   mvn clean test allure:serve
   ```

2. **Enhance other test classes:**
   - Add `@Epic`, `@Feature`, `@Story` annotations
   - Use `@Step` for detailed logging
   - Call `AllureManager.takeScreenshot()` as needed

3. **Customize categories:**
   - Create `src/test/resources/categories.json`
   - Define custom failure categories

4. **Set up CI/CD integration:**
   - Configure Jenkins/GitHub Actions
   - Publish reports automatically

---

## 📚 Additional Resources

- **Allure Documentation:** https://docs.qameta.io/allure/
- **Allure GitHub:** https://github.com/allure-framework
- **TestNG Integration:** https://docs.qameta.io/allure/#_testng
- **Examples:** https://github.com/allure-examples

---

**Happy Testing! 🚀**

*Generated on: February 12, 2026*
