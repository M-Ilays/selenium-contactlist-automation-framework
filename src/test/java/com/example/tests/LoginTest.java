package com.example.tests;

import com.example.pages.LoginPage;
import com.example.utils.AllureManager;
import com.example.utils.BaseClass;
import com.example.utils.ExcelUtils;
import io.qameta.allure.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Epic("User Authentication")
@Feature("Login Functionality")
public class LoginTest extends BaseClass {

    static LoginPage loginPage;

    @BeforeClass
    @Step("Setup Test Environment")
    public void setUp() {
        System.out.println("\n[SETUP] Initializing WebDriver...");
        initializeDriver();
        
        // Add environment info to Allure report
        AllureManager.setEnvironmentInfo("Browser", getProperty("browser"));
        AllureManager.setEnvironmentInfo("Base URL", getProperty("url"));
        AllureManager.setEnvironmentInfo("Java Version", System.getProperty("java.version"));
        AllureManager.setEnvironmentInfo("OS", System.getProperty("os.name"));
        
        System.out.println("[SETUP] Navigating to login page: " + getProperty("url"));
        driver.get(getProperty("url"));
        System.out.println("[SETUP] Creating LoginPage instance...");
        loginPage = new LoginPage();
        System.out.println("[SETUP] Setup completed successfully\n");
    }

    @AfterMethod
    public void cleanup() {
        System.out.println("\n[CLEANUP] Navigating back to login page...");
        driver.get(getProperty("url"));
        System.out.println("[CLEANUP] Ready for next test\n");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("\n[TEARDOWN] Closing browser after all login tests...\n");
        super.tearDown();
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        Properties prop = new Properties();
        try {
            InputStream fis = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (fis != null) {
                prop.load(fis);
            } else {
                throw new IOException("config.properties not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, String>> data = ExcelUtils.getTestData(prop.getProperty("loginData"), "Login");
        Object[][] testData = new Object[data.size()][3];
        
        System.out.println("\n========== READING TEST DATA FROM EXCEL ==========\n");
        
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);
            
            // Debug: Print all keys in the map
            System.out.println("Row " + (i+1) + " - Available columns: " + row.keySet());
            
            testData[i][0] = row.get("Email");
            testData[i][1] = row.get("Password");
            
            // Try to get ExpectedResult with case-insensitive matching (handle spaces)
            String expectedResult = null;
            for (String key : row.keySet()) {
                if (key != null && key.trim().replace(" ", "").equalsIgnoreCase("ExpectedResult")) {
                    expectedResult = row.get(key);
                    break;
                }
            }
            
            // Default to "Pass" if not found
            testData[i][2] = (expectedResult != null && !expectedResult.trim().isEmpty()) ? expectedResult.trim() : "Pass";
            
            System.out.println("Row " + (i+1) + " - Email: " + testData[i][0] + 
                             ", Password: " + testData[i][1] + 
                             ", ExpectedResult: " + testData[i][2]);
        }
        
        System.out.println("\n================================================\n");
        
        return testData;
    }

    @Test(dataProvider = "loginData")
    @Story("User Login Validation")
    @Description("Test login functionality with valid and invalid credentials")
    @Severity(SeverityLevel.BLOCKER)
    public void testLogin(String email, String password, String expectedResult) throws Exception {
        // Set test parameters in Allure report
        Allure.parameter("Email", email);
        Allure.parameter("Password", "****");
        Allure.parameter("Expected Result", expectedResult);
        
        Allure.step("Enter credentials and submit login", () -> {
            loginPage.login(email, password);
        });
        
        // Wait for page to respond
        Thread.sleep(2000);
        
        String currentUrl = driver.getCurrentUrl();
        
        if ("Pass".equalsIgnoreCase(expectedResult)) {
            validateSuccessfulLogin(email, currentUrl);
        } else if ("Fail".equalsIgnoreCase(expectedResult)) {
            validateFailedLogin(email, currentUrl);
        }
    }

    @Step("Validate Successful Login for {email}")
    private void validateSuccessfulLogin(String email, String currentUrl) throws Exception {
        // Positive Test Case - Valid Credentials
        try {
            WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            
            Allure.step("Wait for page navigation", () -> {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("contactList"),
                    ExpectedConditions.not(ExpectedConditions.urlContains("login"))
                ));
            });
            
            // Get updated URL
            final String finalCurrentUrl = driver.getCurrentUrl();
            
            Allure.step("Verify URL contains 'contactList'", () -> {
                Assert.assertTrue(finalCurrentUrl.contains("contactList"), 
                        "Expected URL to contain 'contactList' but got: " + finalCurrentUrl);
            });
            
            Allure.step("Verify URL does not contain 'login'", () -> {
                Assert.assertFalse(finalCurrentUrl.contains("login"), 
                        "Expected to leave login page but still on: " + finalCurrentUrl);
            });
            
            Allure.step("Verify login status", () -> {
                Assert.assertTrue(loginPage.isLoginSuccessful(), 
                        "Login verification failed - still on login page");
            });
            
            String pageTitle = driver.getTitle();
            Allure.step("Verify page title is loaded: " + pageTitle, () -> {
                Assert.assertNotNull(pageTitle, "Page title should not be null after login");
            });
            
            // Display custom success notification on the browser screen
            loginPage.displaySuccessNotification("LOGIN DONE SUCCESSFULLY!");
            
            // Display success message in console
            System.out.println("\n" + "=".repeat(60));
            System.out.println("        ✓ LOGIN DONE SUCCESSFULLY!");
            System.out.println("=".repeat(60));
            System.out.println("User Email: " + email);
            System.out.println("Navigated to: " + finalCurrentUrl);
            System.out.println("Page Title: " + pageTitle);
            System.out.println("Test Status: PASSED ✓");
            System.out.println("All Assertions: PASSED ✓");
            System.out.println("=".repeat(60) + "\n");
            
            // Attach info to Allure report
            Allure.addAttachment("Login Details", "text/html", 
                "<b>User:</b> " + email + "<br>" +
                "<b>URL:</b> " + finalCurrentUrl + "<br>" +
                "<b>Page Title:</b> " + pageTitle + "<br>" +
                "<b>Status:</b> <span style='color:green'>SUCCESS</span>", ".html");
            
            // Take screenshot for visual verification
            takeScreenshot("LoginSuccess_" + email.replace("@", "_"));
            AllureManager.takeScreenshot(driver);
            
        } catch (Exception e) {
            handleUnexpectedFailure(email, e);
        }
    }

    @Step("Validate Failed Login for {email}")
    private void validateFailedLogin(String email, String currentUrl) throws Exception {
        // Negative Test Case - Invalid Credentials
        Thread.sleep(2000); // Wait for error message to appear
        
        final String finalCurrentUrl = currentUrl;
        
        Allure.step("Verify user remains on login page", () -> {
            Assert.assertTrue(finalCurrentUrl.contains("login"), 
                    "Expected to stay on login page but navigated to: " + finalCurrentUrl);
        });
        
        Allure.step("Verify contactList page is not accessed", () -> {
            Assert.assertFalse(finalCurrentUrl.contains("contactList"), 
                    "Should not have accessed contactList with invalid credentials");
        });
        
        // Get error message from page
        String errorMsg = loginPage.getErrorMessage();
        final String finalErrorMsg = errorMsg;
        
        Allure.step("Verify error message is displayed: " + errorMsg, () -> {
            Assert.assertNotNull(finalErrorMsg, "Error message should not be null");
            Assert.assertFalse(finalErrorMsg.isEmpty(), 
                    "Expected error message for invalid credentials but none was found");
        });
        
        Allure.step("Verify error message indicates authentication failure", () -> {
            Assert.assertTrue(
                finalErrorMsg.toLowerCase().contains("incorrect") || 
                finalErrorMsg.toLowerCase().contains("invalid") || 
                finalErrorMsg.toLowerCase().contains("wrong") ||
                finalErrorMsg.toLowerCase().contains("unauthorized"),
                "Error message should indicate authentication failure. Got: " + finalErrorMsg
            );
        });
        
        Allure.step("Verify login was not successful", () -> {
            Assert.assertFalse(loginPage.isLoginSuccessful(), 
                    "Login should not be successful with invalid credentials");
        });
        
        // Display custom error notification on screen
        loginPage.displayErrorNotification("LOGIN FAILED - INVALID CREDENTIALS!");
        
        // Display failure message in console
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        ✗ LOGIN FAILED AS EXPECTED");
        System.out.println("=".repeat(60));
        System.out.println("User Email: " + email);
        System.out.println("Error Message: " + errorMsg);
        System.out.println("Current URL: " + finalCurrentUrl);
        System.out.println("Test Status: PASSED ✓ (Negative case validated)");
        System.out.println("All Assertions: PASSED ✓");
        System.out.println("=".repeat(60) + "\n");
        
        // Attach info to Allure report
        Allure.addAttachment("Login Failure Details", "text/html", 
            "<b>User:</b> " + email + "<br>" +
            "<b>Error:</b> " + errorMsg + "<br>" +
            "<b>URL:</b> " + finalCurrentUrl + "<br>" +
            "<b>Status:</b> <span style='color:orange'>EXPECTED FAILURE</span>", ".html");
        
        // Take screenshot for visual verification
        takeScreenshot("LoginFailure_" + email.replace("@", "_"));
        AllureManager.takeScreenshot(driver);
    }

    @Step("Handle Unexpected Login Failure for {email}")
    private void handleUnexpectedFailure(String email, Exception e) throws Exception {
        String currentUrl = driver.getCurrentUrl();
        String errorMsg = loginPage.getErrorMessage();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        ⚠ LOGIN FAILED - BUT EXPECTED TO PASS!");
        System.out.println("=".repeat(60));
        System.out.println("User Email: " + email);
        System.out.println("Current URL: " + currentUrl);
        System.out.println("Error Message: " + errorMsg);
        System.out.println("Note: Check if credentials are correct or ExpectedResult should be 'Fail'");
        System.out.println("=".repeat(60) + "\n");
        
        // Attach failure details to Allure
        Allure.addAttachment("Unexpected Failure Details", "text/html", 
            "<b>User:</b> " + email + "<br>" +
            "<b>Error:</b> " + errorMsg + "<br>" +
            "<b>URL:</b> " + currentUrl + "<br>" +
            "<b>Exception:</b> " + e.getMessage() + "<br>" +
            "<b>Status:</b> <span style='color:red'>UNEXPECTED FAILURE</span>", ".html");
        
        takeScreenshot("LoginUnexpectedFailure_" + email.replace("@", "_"));
        AllureManager.takeScreenshot(driver);
        throw e; // Re-throw to fail the test
    }

    private void takeScreenshot(String fileName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File source = screenshot.getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File destination = new File("screenshots/" + fileName + "_" + timestamp + ".png");
            destination.getParentFile().mkdirs();
            FileHandler.copy(source, destination);
            System.out.println("Screenshot saved: " + destination.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}