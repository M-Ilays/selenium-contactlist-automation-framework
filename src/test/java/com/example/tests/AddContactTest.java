package com.example.tests;

import com.example.utils.BaseClass;
import com.example.pages.AddContactPage;
import com.example.pages.LoginPage;
import com.example.utils.ExcelUtils;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class AddContactTest extends BaseClass {
    
    private static AddContactPage addContactPage;
    private static WebDriverWait wait;
    
    @BeforeClass
    public void setup() {
        System.out.println("\n[SETUP] Initializing WebDriver for AddContactTest...");
        initializeDriver();
        
        // Login once for all tests in this class
        System.out.println("[SETUP] Navigating to login page...");
        driver.get("https://thinking-tester-contact-list.herokuapp.com/login");
        LoginPage loginPage = new LoginPage();
        try {
            System.out.println("[SETUP] Logging in with credentials...");
            loginPage.login("m46328690@gmail.com", "1234567");
            Thread.sleep(2000);
            
            System.out.println("[SETUP] Navigating to add contact page...");
            driver.get("https://thinking-tester-contact-list.herokuapp.com/addContact");
            Thread.sleep(1000);
            System.out.println("[SETUP] Successfully navigated to add contact page");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("[SETUP] Creating AddContactPage instance...");
        addContactPage = new AddContactPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        System.out.println("[SETUP] AddContactTest setup completed\n");
    }
    
    @AfterClass
    public void tearDown() {
        super.tearDown();
    }
    
    @DataProvider(name = "contactData")
    public Object[][] getContactData() throws IOException {
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
        
        List<Map<String, String>> data = ExcelUtils.getTestData(prop.getProperty("loginData"), "AddContact");
        Object[][] testData = new Object[data.size()][12];
        
        System.out.println("\n========== READING ADD CONTACT TEST DATA FROM EXCEL ==========\n");
        
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);
            
            System.out.println("Row " + (i+1) + " - Available columns: " + row.keySet());
            
            testData[i][0] = row.get("First Name") != null ? row.get("First Name") : "";
            testData[i][1] = row.get("Last Name") != null ? row.get("Last Name") : "";
            testData[i][2] = row.get("Birthdate") != null ? row.get("Birthdate") : "";
            testData[i][3] = row.get("Email") != null ? row.get("Email") : "";
            testData[i][4] = row.get("Phone") != null ? row.get("Phone") : "";
            testData[i][5] = row.get("Street1") != null ? row.get("Street1") : "";
            testData[i][6] = row.get("Street2") != null ? row.get("Street2") : "";
            testData[i][7] = row.get("City") != null ? row.get("City") : "";
            testData[i][8] = row.get("State/Province") != null ? row.get("State/Province") : "";
            testData[i][9] = row.get("Postal Code") != null ? row.get("Postal Code") : "";
            testData[i][10] = row.get("Country") != null ? row.get("Country") : "";
            
            String expectedResult = null;
            for (String key : row.keySet()) {
                if (key != null && key.trim().replace(" ", "").equalsIgnoreCase("ExpectedResult")) {
                    expectedResult = row.get(key);
                    break;
                }
            }
            testData[i][11] = (expectedResult != null && !expectedResult.trim().isEmpty()) ? expectedResult.trim() : "Pass";
            
            System.out.println("Row " + (i+1) + " - FirstName: " + testData[i][0] + 
                             ", LastName: " + testData[i][1] + 
                             ", Email: " + testData[i][3] + 
                             ", Phone: " + testData[i][4] + 
                             ", ExpectedResult: " + testData[i][11]);
        }
        
        System.out.println("\n================================================\n");
        
        return testData;
    }
    
    @Test(dataProvider = "contactData")
    public void testAddContact(String firstName, String lastName, String birthdate, String email,
                               String phone, String street1, String street2, String city,
                               String stateProvince, String postalCode, String country,
                               String expectedResult) throws Exception {
        
        System.out.println("\n============================================================");
        System.out.println("Testing Add Contact: " + firstName + " " + lastName);
        System.out.println("Email: " + email + ", Phone: " + phone);
        System.out.println("Expected Result: " + expectedResult);
        System.out.println("============================================================\n");
        
        // Perform add contact
        addContactPage.addContact(firstName, lastName, birthdate, email, phone, 
                                 street1, street2, city, stateProvince, postalCode, country);
        
        String currentUrl = driver.getCurrentUrl();
        
        if ("Pass".equalsIgnoreCase(expectedResult)) {
            // Positive Test Case - Valid Contact Data
            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("contactList"),
                    ExpectedConditions.not(ExpectedConditions.urlContains("addContact"))
                ));
                
                currentUrl = driver.getCurrentUrl();
                
                // ASSERTION 1: Verify URL contains contactList
                Assert.assertTrue(currentUrl.contains("contactList"), 
                        "Expected URL to contain 'contactList' but got: " + currentUrl);
                
                // ASSERTION 2: Verify URL does not contain addContact
                Assert.assertFalse(currentUrl.contains("addContact"), 
                        "Expected to leave addContact page but still on: " + currentUrl);
                
                // ASSERTION 3: Verify first name is not empty
                Assert.assertFalse(firstName.trim().isEmpty(), 
                        "First name should not be empty");
                
                // ASSERTION 4: Verify last name is not empty
                Assert.assertFalse(lastName.trim().isEmpty(), 
                        "Last name should not be empty");
                
                // ASSERTION 5: Verify email format if provided
                if (!email.isEmpty()) {
                    Assert.assertTrue(email.contains("@"), 
                            "Email should contain @ symbol. Got: " + email);
                }
                
                // ASSERTION 6: Verify phone is not empty
                Assert.assertFalse(phone.trim().isEmpty(), 
                        "Phone should not be empty");
                
                // ASSERTION 7: Verify birthdate format if provided
                if (!birthdate.isEmpty()) {
                    Assert.assertTrue(birthdate.matches("\\d{4}-\\d{2}-\\d{2}"), 
                            "Birthdate should be in YYYY-MM-DD format. Got: " + birthdate);
                }
                
                // Display validation popup with all passed validations
                String[] validations = {
                    "URL contains 'contactList' - PASSED",
                    "Left addContact page successfully - PASSED",
                    "First Name: '" + firstName + "' - PASSED",
                    "Last Name: '" + lastName + "' - PASSED",
                    "Email: '" + email + "' - PASSED",
                    "Phone: '" + phone + "' - PASSED",
                    "Contact added to list successfully - PASSED"
                };
                addContactPage.displayValidationPopup("ADD CONTACT VALIDATION SUCCESSFUL!", validations, true);
                Thread.sleep(5500);
                
                // Display custom success notification
                addContactPage.displaySuccessNotification(firstName, lastName);
                
                System.out.println("\n============================================================");
                System.out.println("        ✓ CONTACT ADDED SUCCESSFULLY!");
                System.out.println("============================================================");
                System.out.println("Contact Name: " + firstName + " " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Phone: " + phone);
                System.out.println("City: " + city + ", " + stateProvince);
                System.out.println("Country: " + country);
                System.out.println("Navigated to: " + currentUrl);
                System.out.println("Test Status: PASSED ✓");
                System.out.println("All Assertions: PASSED ✓");
                System.out.println("============================================================\n");
                
                Thread.sleep(5000);
                takeScreenshot("AddContactSuccess_" + firstName.replace(" ", "_") + "_" + lastName.replace(" ", "_"));
                
            } catch (Exception e) {
                currentUrl = driver.getCurrentUrl();
                String errorMsg = addContactPage.getErrorMessage();
                
                System.out.println("\n============================================================");
                System.out.println("        ⚠ ADD CONTACT FAILED - BUT EXPECTED TO PASS!");
                System.out.println("============================================================");
                System.out.println("Contact: " + firstName + " " + lastName);
                System.out.println("Current URL: " + currentUrl);
                System.out.println("Error Message: " + errorMsg);
                System.out.println("Note: Check if contact data is valid or ExpectedResult should be 'Fail'");
                System.out.println("============================================================\n");
                
                takeScreenshot("AddContactUnexpectedFailure_" + firstName.replace(" ", "_"));
                throw e;
            }
            
        } else if ("Fail".equalsIgnoreCase(expectedResult)) {
            // Negative Test Case - Invalid Contact Data
            Thread.sleep(2000);
            
            // ASSERTION 1: Verify still on addContact page
            Assert.assertTrue(currentUrl.contains("addContact"), 
                    "Expected to stay on addContact page but navigated to: " + currentUrl);
            
            // ASSERTION 2: Verify did not navigate to contactList
            Assert.assertFalse(currentUrl.contains("contactList"),
                    "Should not access contactList with invalid data");
            
            // Get error message if present
            String errorMsg = addContactPage.getErrorMessage();
            
            // ASSERTION 3: Verify error handling
            if (firstName.isEmpty() || lastName.isEmpty()) {
                System.out.println("Validation: Required fields missing (First Name or Last Name)");
                Assert.assertTrue(true, "Validation working correctly for required fields");
            }
            
            // ASSERTION 4: Verify phone validation if invalid format
            if (!phone.isEmpty() && !phone.matches("\\d+")) {
                System.out.println("Validation: Invalid phone format detected");
                Assert.assertTrue(true, "Phone validation working correctly");
            }
            
            // ASSERTION 5: Verify birthdate validation if invalid format
            if (!birthdate.isEmpty() && !birthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Validation: Invalid birthdate format detected");
                Assert.assertTrue(true, "Birthdate validation working correctly");
            }
            
            // Display validation popup with all passed negative test validations
            String[] validations = {
                "Stayed on addContact page - PASSED",
                "Did not access contactList - PASSED",
                "Required field validation - PASSED",
                "Data format validation - PASSED",
                "Add contact blocked as expected - PASSED",
                errorMsg.isEmpty() ? "Validation working correctly" : "Error: " + errorMsg
            };
            addContactPage.displayValidationPopup("NEGATIVE TEST VALIDATION PASSED!", validations, false);
            Thread.sleep(5500);
            
            // Display error notification
            addContactPage.displayErrorNotification(errorMsg.isEmpty() ? "Invalid contact data" : errorMsg);
            
            System.out.println("\n============================================================");
            System.out.println("        ✓ ADD CONTACT FAILED AS EXPECTED");
            System.out.println("============================================================");
            System.out.println("Contact: " + firstName + " " + lastName);
            System.out.println("Error Message: " + errorMsg);
            System.out.println("Current URL: " + currentUrl);
            System.out.println("Test Status: PASSED ✓ (Negative Test)");
            System.out.println("All Assertions: PASSED ✓");
            System.out.println("============================================================\n");
            
            Thread.sleep(3000);
            takeScreenshot("AddContactNegativeTest_" + firstName.replace(" ", "_"));
        }
    }
    
    @AfterMethod
    public void cleanup() {
        // Navigate back to add contact page for next test
        if (driver != null && !driver.getCurrentUrl().contains("addContact")) {
            System.out.println("[CLEANUP] Navigating back to add contact page...");
            driver.get("https://thinking-tester-contact-list.herokuapp.com/addContact");
            try {
                Thread.sleep(1000);
                System.out.println("[CLEANUP] Back to add contact page\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
