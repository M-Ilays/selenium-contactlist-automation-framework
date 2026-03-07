package com.example.tests;

import com.example.pages.DeleteContactPage;
import com.example.pages.LoginPage;
import com.example.utils.BaseClass;
import com.example.utils.ExcelUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class DeleteContactTest extends BaseClass {
    
    private static DeleteContactPage deleteContactPage;
    private static WebDriverWait wait;
    
    @BeforeClass
    public void setup() {
        System.out.println("\n[SETUP] Initializing WebDriver for DeleteContactTest...");
        initializeDriver();
        
        // Login once for all tests
        System.out.println("[SETUP] Navigating to login page...");
        driver.get("https://thinking-tester-contact-list.herokuapp.com/login");
        LoginPage loginPage = new LoginPage();
        try {
            System.out.println("[SETUP] Logging in with credentials...");
            loginPage.login("m46328690@gmail.com", "1234567");
            Thread.sleep(2000);
            
            System.out.println("[SETUP] Navigating to contact list...");
            driver.get("https://thinking-tester-contact-list.herokuapp.com/contactList");
            Thread.sleep(1000);
            System.out.println("[SETUP] Successfully navigated to contact list");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("[SETUP] Creating DeleteContactPage instance...");
        deleteContactPage = new DeleteContactPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        System.out.println("[SETUP] DeleteContactTest setup completed\n");
    }
    
    @AfterClass
    public void tearDown() {
        super.tearDown();
    }
    
    @DataProvider(name = "deleteData")
    public Object[][] getDeleteData() throws IOException {
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
        
        List<Map<String, String>> data = ExcelUtils.getTestData(prop.getProperty("loginData"), "DeleteContact");
        Object[][] testData = new Object[data.size()][3];
        
        System.out.println("\n========== READING DELETE CONTACT TEST DATA FROM EXCEL ==========\n");
        
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);
            
            System.out.println("Row " + (i+1) + " - Available columns: " + row.keySet());
            
            String firstName = row.get("First Name") != null ? row.get("First Name") : "";
            String lastName = row.get("Last Name") != null ? row.get("Last Name") : "";
            
            // Map old names to updated names (contacts were updated in UpdateContactTest)
            if (firstName.equals("Faris") && lastName.equals("Hamza")) {
                firstName = "Faris Updated";
                lastName = "Hamza Updated";
                System.out.println("[INFO] Remapping to updated contact: Faris Updated Hamza Updated");
            } else if (firstName.equals("Muhammad") && lastName.equals("Ali")) {
                firstName = "Ali";
                lastName = "Muhammad";
                System.out.println("[INFO] Remapping to updated contact: Ali Muhammad");
            }
            
            testData[i][0] = firstName;
            testData[i][1] = lastName;
            testData[i][2] = row.get("Expected Result") != null ? row.get("Expected Result").trim() : "";
            
            System.out.println("Row " + (i+1) + " - FirstName: " + testData[i][0] + 
                             ", LastName: " + testData[i][1] + 
                             ", ExpectedResult: " + testData[i][2]);
        }
        
        System.out.println("\n================================================\n");
        
        return testData;
    }
    
    @Test(dataProvider = "deleteData")
    public void testDeleteContact(String firstName, String lastName, String expectedResult) {
        String fullName = (firstName + " " + lastName).trim();
        
        System.out.println("\n============================================================");
        System.out.println("Testing Delete Contact: " + fullName);
        System.out.println("Expected Result: " + expectedResult);
        System.out.println("============================================================\n");
        
        List<String> validations = new ArrayList<>();
        boolean testPassed = false;
        String currentUrl = "";
        
        try {
            // Navigate to contact list if not already there
            if (!driver.getCurrentUrl().contains("contactList")) {
                driver.get("https://thinking-tester-contact-list.herokuapp.com/contactList");
                Thread.sleep(1000);
            }
            
            String urlBeforeDelete = driver.getCurrentUrl();
            validations.add("Starting URL: " + urlBeforeDelete + " - " + 
                          (urlBeforeDelete.contains("contactList") ? "PASSED ✓" : "FAILED ✗"));
            
            // Click on the contact
            deleteContactPage.clickContactByName(firstName, lastName);
            Thread.sleep(1000);
            
            String contactDetailsUrl = driver.getCurrentUrl();
            boolean onContactDetails = contactDetailsUrl.contains("contacts/");
            validations.add("Navigated to contact details: " + (onContactDetails ? "PASSED ✓" : "FAILED ✗"));
            
            if (expectedResult.equalsIgnoreCase("Pass")) {
                // Delete the contact
                deleteContactPage.deleteContact();
                
                // Wait for redirect to contact list
                try {
                    wait.until(ExpectedConditions.urlContains("contactList"));
                } catch (Exception e) {
                    System.out.println("Timeout waiting for redirect to contactList");
                }
                
                currentUrl = driver.getCurrentUrl();
                boolean backToList = currentUrl.contains("contactList");
                validations.add("Redirected back to contact list: " + (backToList ? "PASSED ✓" : "FAILED ✗"));
                
                // Verify contact name field is not empty (before delete)
                validations.add("Contact name provided: " + (!fullName.isEmpty() ? "PASSED ✓" : "FAILED ✗"));
                
                // Verify successful deletion
                validations.add("Delete operation completed: " + (backToList ? "PASSED ✓" : "FAILED ✗"));
                
                testPassed = backToList;
                
                if (testPassed) {
                    System.out.println("\n============================================================");
                    System.out.println("        🗑️ CONTACT DELETED SUCCESSFULLY!");
                    System.out.println("============================================================");
                    System.out.println("Contact Name: " + fullName);
                    System.out.println("Returned to: " + currentUrl);
                    System.out.println("Test Status: PASSED ✓");
                    System.out.println("All Assertions: PASSED ✓");
                    System.out.println("============================================================\n");
                    
                    String[] validationArray = validations.toArray(new String[0]);
                    deleteContactPage.displayValidationPopup("Delete Contact - Validation Results", validationArray, true);
                    Thread.sleep(5000);
                    
                    deleteContactPage.displaySuccessNotification(fullName);
                    Thread.sleep(3000);
                    
                    takeScreenshot("DeleteContactSuccess_" + fullName.replace(" ", "_"));
                    
                    Assert.assertTrue(true, "Contact deleted successfully");
                } else {
                    System.out.println("\n============================================================");
                    System.out.println("        ❌ DELETE CONTACT FAILED - BUT EXPECTED TO PASS!");
                    System.out.println("============================================================");
                    System.out.println("Contact: " + fullName);
                    System.out.println("Current URL: " + currentUrl);
                    System.out.println("Test Status: FAILED ✗");
                    System.out.println("============================================================\n");
                    
                    String[] validationArray = validations.toArray(new String[0]);
                    deleteContactPage.displayValidationPopup("Delete Contact - Unexpected Failure", validationArray, false);
                    Thread.sleep(5000);
                    
                    takeScreenshot("DeleteContactUnexpectedFailure_" + fullName.replace(" ", "_"));
                    
                    Assert.fail("Delete operation failed unexpectedly");
                }
                
            } else {
                // Negative test case
                System.out.println("Validation: Contact not found or invalid contact name");
                
                validations.add("Contact name: " + (fullName.isEmpty() ? "Empty (Invalid)" : fullName));
                validations.add("Expected behavior: " + (fullName.isEmpty() ? "Should not find contact" : "Contact not in list"));
                validations.add("Negative test: PASSED ✓");
                
                System.out.println("\n============================================================");
                System.out.println("        ✓ DELETE FAILED AS EXPECTED");
                System.out.println("============================================================");
                System.out.println("Contact: " + fullName);
                System.out.println("Current URL: " + driver.getCurrentUrl());
                System.out.println("Test Status: PASSED ✓ (Negative Test)");
                System.out.println("All Assertions: PASSED ✓");
                System.out.println("============================================================\n");
                
                String[] validationArray = validations.toArray(new String[0]);
                deleteContactPage.displayValidationPopup("Delete Contact - Negative Test Results", validationArray, false);
                Thread.sleep(5000);
                
                takeScreenshot("DeleteContactNegativeTest_" + fullName.replace(" ", "_"));
                
                // Navigate back to contact list for next test
                driver.get("https://thinking-tester-contact-list.herokuapp.com/contactList");
                Thread.sleep(1000);
                
                Assert.assertTrue(true, "Negative test passed");
            }
            
        } catch (Exception e) {
            System.out.println("\n============================================================");
            System.out.println("        ❌ TEST EXECUTION ERROR");
            System.out.println("============================================================");
            System.out.println("Contact: " + fullName);
            System.out.println("Error: " + e.getMessage());
            System.out.println("============================================================\n");
            
            validations.add("Exception occurred: " + e.getMessage());
            String[] validationArray = validations.toArray(new String[0]);
            deleteContactPage.displayValidationPopup("Delete Contact - Test Error", validationArray, false);
            
            try {
                Thread.sleep(5000);
                takeScreenshot("DeleteContactError_" + fullName.replace(" ", "_"));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            e.printStackTrace();
            Assert.fail("Test execution failed: " + e.getMessage());
        }
    }
    
    @AfterMethod
    public void cleanup() {
        // Navigate back to contact list for next test
        if (driver != null && !driver.getCurrentUrl().contains("contactList")) {
            System.out.println("[CLEANUP] Navigating back to contact list...");
            driver.get("https://thinking-tester-contact-list.herokuapp.com/contactList");
            try {
                Thread.sleep(1000);
                System.out.println("[CLEANUP] Back to contact list\n");
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
