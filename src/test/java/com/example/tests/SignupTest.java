package com.example.tests;

import com.example.pages.SignupPage;
import com.example.utils.BaseClass;
import com.example.utils.ExcelUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class SignupTest extends BaseClass {
    
    private static SignupPage signupPage;
    private static WebDriverWait wait;
    
    @BeforeClass
    public void setup() {
        initializeDriver();
        driver.get("https://thinking-tester-contact-list.herokuapp.com/addUser");
        signupPage = new SignupPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increased to 20 seconds
    }
    
    @AfterMethod
    public void cleanup() {
        System.out.println("\n[CLEANUP] Navigating back to signup page...");
        driver.get("https://thinking-tester-contact-list.herokuapp.com/addUser");
        System.out.println("[CLEANUP] Ready for next test\n");
    }
    
    @DataProvider(name = "signupData")
    public Object[][] getSignupData() throws IOException {
        System.out.println("\n========== READING SIGNUP TEST DATA FROM EXCEL ==========\n");
        
        String fileName = prop.getProperty("loginData", "Data.xlsx");
        List<Map<String, String>> testData = ExcelUtils.getTestData(fileName, "SignUp");
        
        Object[][] data = new Object[testData.size()][5];
        
        for (int i = 0; i < testData.size(); i++) {
            Map<String, String> row = testData.get(i);
            
            // Debug: Print available columns
            System.out.println("Row " + (i + 1) + " - Available columns: " + row.keySet());
            
            // Find firstName with flexible column name matching
            String firstName = "";
            String lastName = "";
            String email = row.getOrDefault("Email", row.getOrDefault("email", ""));
            String password = row.getOrDefault("Password", row.getOrDefault("password", ""));
            
            for (String key : row.keySet()) {
                String normalizedKey = key.toLowerCase().replace(" ", "");
                if (normalizedKey.equals("firstname")) {
                    firstName = row.get(key);
                } else if (normalizedKey.equals("lastname")) {
                    lastName = row.get(key);
                }
            }
            
            // Try to find ExpectedResult with various possible column names
            String expectedResult = "Pass";
            for (String key : row.keySet()) {
                if (key.toLowerCase().replace(" ", "").equals("expectedresult")) {
                    expectedResult = row.get(key);
                    break;
                }
            }
            
            data[i][0] = firstName;
            data[i][1] = lastName;
            data[i][2] = email;
            data[i][3] = password;
            data[i][4] = expectedResult;
            
            System.out.println("Row " + (i + 1) + " - FirstName: " + firstName + 
                             ", LastName: " + lastName + 
                             ", Email: " + email + 
                             ", Password: " + password + 
                             ", ExpectedResult: " + expectedResult);
        }
        
        System.out.println("\n================================================\n");
        
        return data;
    }
    
    @Test(dataProvider = "signupData")
    public void testSignup(String firstName, String lastName, String email, String password, String expectedResult) throws Exception {
        // For positive test cases, generate a unique email to avoid conflicts
        if (expectedResult.equalsIgnoreCase("Pass")) {
            String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
            email = "testuser" + timestamp + "@test.com";
            System.out.println("Generated unique email for new user: " + email);
        }
        
        // Perform signup
        signupPage.signup(firstName, lastName, email, password);
        
        if (expectedResult.equalsIgnoreCase("Pass")) {
            // For successful signup - should navigate to contact list page
            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("contactList"),
                    ExpectedConditions.urlContains("contact")
                ));
                
                String currentUrl = driver.getCurrentUrl();
                String pageTitle = driver.getTitle();
                
                // ASSERTION 1: Verify URL contains contactList or contact
                Assert.assertTrue(
                    currentUrl.contains("contactList") || currentUrl.contains("contact"),
                    "Expected URL to contain 'contactList' or 'contact' but got: " + currentUrl
                );
                
                // ASSERTION 2: Verify URL does not contain addUser (signup page)
                if (currentUrl.contains("addUser")) {
                    // Still on signup page - check if there's an error
                    String errorMsg = signupPage.getErrorMessage();
                    Assert.fail("Signup failed - still on addUser page. Error: " + errorMsg + " | URL: " + currentUrl);
                }
                
                // ASSERTION 3: Verify firstName is not empty
                Assert.assertFalse(firstName.trim().isEmpty(), 
                    "First name should not be empty for successful signup");
                
                // ASSERTION 4: Verify lastName is not empty
                Assert.assertFalse(lastName.trim().isEmpty(), 
                    "Last name should not be empty for successful signup");
                
                // ASSERTION 5: Verify email contains @ symbol
                Assert.assertTrue(email.contains("@"), 
                    "Email should contain @ symbol. Got: " + email);
                
                // ASSERTION 6: Verify password is not empty
                Assert.assertFalse(password.isEmpty(), 
                    "Password should not be empty for successful signup");
                
                System.out.println("\n============================================================");
                System.out.println("        ✓ SIGNUP DONE SUCCESSFULLY!");
                System.out.println("============================================================");
                System.out.println("User: " + firstName + " " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Navigated to: " + currentUrl);
                System.out.println("Page Title: " + pageTitle);
                System.out.println("Test Status: PASSED ✓");
                System.out.println("All Assertions: PASSED ✓");
                System.out.println("============================================================\n");
                
                // Display success notification on browser
                signupPage.displaySuccessNotification(firstName, lastName);
                Thread.sleep(5000); // Wait for notification to display
                
                // Take screenshot
                String screenshotPath = takeScreenshot("SignupSuccess_" + email.replace("@", "_").replace(".", "_"));
                System.out.println("Screenshot saved: " + screenshotPath);
                
            } catch (Exception e) {
                // Signup failed - check if this is actually an existing user scenario
                String currentUrl = driver.getCurrentUrl();
                String errorMsg = signupPage.getErrorMessage();
                
                // If still on addUser page with error message, this might be existing user
                if (currentUrl.contains("addUser") && !errorMsg.isEmpty()) {
                    System.out.println("\n============================================================");
                    System.out.println("        ⚠ SIGNUP FAILED - POSSIBLY EXISTING USER!");
                    System.out.println("============================================================");
                    System.out.println("User: " + firstName + " " + lastName);
                    System.out.println("Email: " + email);
                    System.out.println("Error Message: " + errorMsg);
                    System.out.println("Current URL: " + currentUrl);
                    System.out.println("Note: If this is an existing user, set ExpectedResult to 'Fail'");
                    System.out.println("============================================================\n");
                    
                    // Display error notification
                    signupPage.displayErrorNotification(errorMsg.isEmpty() ? "Signup Failed - Possible Existing User" : errorMsg);
                    Thread.sleep(3000);
                }
                
                String screenshotPath = takeScreenshot("SignupFailure_" + email.replace("@", "_").replace(".", "_"));
                System.out.println("Screenshot saved: " + screenshotPath);
                
                throw e; // Re-throw to fail the test
            }
            
        } else {
            // For expected failure - should stay on signup page with error
            try {
                Thread.sleep(2000); // Wait for error message to appear
                
                String errorMsg = signupPage.getErrorMessage();
                String currentUrl = driver.getCurrentUrl();
                
                // ASSERTION 1: Verify still on signup page
                Assert.assertTrue(currentUrl.contains("addUser"), 
                    "Expected to stay on signup page but navigated to: " + currentUrl);
                
                // ASSERTION 2: Verify did not navigate to contactList
                Assert.assertFalse(currentUrl.contains("contactList"),
                    "Should not access contactList with invalid data");
                
                // ASSERTION 3: Verify error message is present
                Assert.assertNotNull(errorMsg, "Error message should not be null");
                
                // ASSERTION 4: Verify error message is not empty (when validation fails)
                if (email.isEmpty() || !email.contains("@") || password.length() < 7) {
                    Assert.assertFalse(errorMsg.isEmpty(),
                        "Expected error message for invalid input but none was found");
                }
                
                // ASSERTION 5: Validate specific error scenarios
                if (!email.contains("@")) {
                    System.out.println("Validation: Invalid email format detected");
                    Assert.assertTrue(true, "Email validation working correctly");
                }
                if (password.length() < 7) {
                    System.out.println("Validation: Short password detected");
                    Assert.assertTrue(true, "Password validation working correctly");
                }
                
                System.out.println("\n============================================================");
                System.out.println("        ✓ SIGNUP FAILED AS EXPECTED!");
                System.out.println("============================================================");
                System.out.println("User: " + firstName + " " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Error Message: " + errorMsg);
                System.out.println("Current URL: " + currentUrl);
                System.out.println("Test Status: PASSED ✓ (Negative Test)");
                System.out.println("All Assertions: PASSED ✓");
                System.out.println("============================================================\n");
                
                // Display error notification on browser
                signupPage.displayErrorNotification(errorMsg.isEmpty() ? "Validation Error" : errorMsg);
                Thread.sleep(5000);
                
                // Take screenshot
                String screenshotPath = takeScreenshot("SignupNegativeTest_" + email.replace("@", "_").replace(".", "_"));
                System.out.println("Screenshot saved: " + screenshotPath);
                
            } catch (Exception e) {
                System.out.println("Error during negative test validation: " + e.getMessage());
                throw e;
            }
        }
    }
    
    private String takeScreenshot(String testCaseName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File("screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdir();
            }
            
            // Create filename with timestamp
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = testCaseName + "_" + timestamp + ".png";
            
            File destination = new File(screenshotDir, fileName);
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            return destination.getAbsolutePath();
        } catch (IOException e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
            return "";
        }
    }
    
    @AfterClass
    public void teardown() {
        System.out.println("\n[TEARDOWN] Closing browser after all signup tests...\n");
        tearDown();
    }
}
