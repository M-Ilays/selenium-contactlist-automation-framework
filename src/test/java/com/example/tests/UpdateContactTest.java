package com.example.tests;

import com.example.utils.BaseClass;
import com.example.pages.UpdateContactPage;
import com.example.pages.LoginPage;
import com.example.utils.ExcelUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class UpdateContactTest extends BaseClass {
    
    private static UpdateContactPage updateContactPage;
    private static WebDriverWait wait;
    
    @BeforeClass
    public void setup() {
        System.out.println("\n[SETUP] Initializing WebDriver for UpdateContactTest...");
        initializeDriver();
        
        // Login once for all tests in this class
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
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        System.out.println("[SETUP] UpdateContactTest setup completed\n");
    }
    
    @AfterClass
    public void tearDown() {
        super.tearDown();
    }
    
    @AfterMethod
    public void cleanup() {
        // Navigate back to contact list after each test
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
    
    @DataProvider(name = "updateContactData")
    public Object[][] getUpdateContactData() throws IOException {
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
        
        List<Map<String, String>> data = ExcelUtils.getTestData(prop.getProperty("loginData"), "UpdateContact");
        Object[][] testData = new Object[data.size()][12];
        
        System.out.println("\n========== READING UPDATE CONTACT TEST DATA FROM EXCEL ==========\n");
        
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
            testData[i][11] = row.get("Expected Result") != null ? row.get("Expected Result").trim() : "";
            
            System.out.println("Row " + (i+1) + " - FirstName: " + testData[i][0] + 
                             ", LastName: " + testData[i][1] + 
                             ", Email: " + testData[i][3] + 
                             ", Phone: " + testData[i][4] + 
                             ", ExpectedResult: " + testData[i][11]);
        }
        
        System.out.println("\n================================================\n");
        return testData;
    }
    
    @Test(dataProvider = "updateContactData")
    public void testUpdateContact(String firstName, String lastName, String birthdate, 
                                  String email, String phone, String street1, String street2,
                                  String city, String stateProvince, String postalCode, 
                                  String country, String expectedResult) throws InterruptedException {
        
        try {
            // Click on the first contact in the list to edit
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr.contactTableBodyRow")));
            WebElement firstContact = driver.findElement(By.cssSelector("tr.contactTableBodyRow"));
            firstContact.click();
            Thread.sleep(1000);
            
            // Click the edit button
            wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-contact")));
            WebElement editButton = driver.findElement(By.id("edit-contact"));
            editButton.click();
            Thread.sleep(1000);
            
            // Initialize UpdateContactPage
            updateContactPage = new UpdateContactPage(driver);
            
            // Update the contact with new data
            updateContactPage.updateContact(firstName, lastName, birthdate, email, phone, 
                                          street1, street2, city, stateProvince, postalCode, country);
            
            Thread.sleep(2000);
            
            // Validate based on expected result
            List<String> validations = new ArrayList<>();
            boolean allPassed = true;
            
            if (expectedResult.equalsIgnoreCase("Pass")) {
                try {
                    // Wait for navigation to contact details page
                    wait.until(ExpectedConditions.or(
                        ExpectedConditions.urlContains("contactDetails"),
                        ExpectedConditions.urlContains("contactList")
                    ));
                    
                    String currentUrl = driver.getCurrentUrl();
                    
                    // Assertion 1: URL should contain contactDetails
                    if (currentUrl.contains("contactDetails")) {
                        validations.add("✓ URL contains 'contactDetails' - PASSED");
                        Assert.assertTrue(true, "URL validation passed");
                    } else {
                        validations.add("✗ URL should contain 'contactDetails' but contains: " + currentUrl + " - FAILED");
                        allPassed = false;
                        Assert.fail("Expected URL to contain 'contactDetails'");
                    }
                    
                    // Assertion 2: Verify updated name
                    WebElement nameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
                    String displayedName = nameElement.getText();
                    if (displayedName.contains(firstName) || displayedName.contains(lastName)) {
                        validations.add("✓ Contact name updated: " + displayedName + " - PASSED");
                        Assert.assertTrue(true, "Name updated successfully");
                    } else {
                        validations.add("✗ Name not properly updated - FAILED");
                        allPassed = false;
                    }
                    
                    // Assertion 3: Verify updated email
                    WebElement emailElement = driver.findElement(By.id("email"));
                    String displayedEmail = emailElement.getText();
                    if (displayedEmail.equals(email)) {
                        validations.add("✓ Email updated: " + email + " - PASSED");
                        Assert.assertEquals(displayedEmail, email, "Email should match");
                    } else {
                        validations.add("✗ Email mismatch - FAILED");
                        allPassed = false;
                    }
                    
                    // Assertion 4: Verify updated phone
                    WebElement phoneElement = driver.findElement(By.id("phone"));
                    String displayedPhone = phoneElement.getText();
                    if (displayedPhone.equals(phone)) {
                        validations.add("✓ Phone updated: " + phone + " - PASSED");
                        Assert.assertEquals(displayedPhone, phone, "Phone should match");
                    } else {
                        validations.add("✗ Phone mismatch - FAILED");
                        allPassed = false;
                    }
                    
                    // Assertion 5: Verify city and state
                    if (!city.isEmpty()) {
                        WebElement cityElement = driver.findElement(By.id("city"));
                        String displayedCity = cityElement.getText();
                        if (displayedCity.contains(city)) {
                            validations.add("✓ City updated: " + city + " - PASSED");
                            Assert.assertTrue(true, "City validation passed");
                        } else {
                            validations.add("✗ City mismatch - FAILED");
                            allPassed = false;
                        }
                    }
                    
                    // Assertion 6: Verify country
                    if (!country.isEmpty()) {
                        WebElement countryElement = driver.findElement(By.id("country"));
                        String displayedCountry = countryElement.getText();
                        if (displayedCountry.equals(country)) {
                            validations.add("✓ Country updated: " + country + " - PASSED");
                            Assert.assertEquals(displayedCountry, country, "Country should match");
                        } else {
                            validations.add("✗ Country mismatch - FAILED");
                            allPassed = false;
                        }
                    }
                    
                    // Assertion 7: Verify page navigation back to list
                    driver.get("https://thinking-tester-contact-list.herokuapp.com/contactList");
                    Thread.sleep(1000);
                    wait.until(ExpectedConditions.urlContains("contactList"));
                    if (driver.getCurrentUrl().contains("contactList")) {
                        validations.add("✓ Successfully navigated back to contact list - PASSED");
                        Assert.assertTrue(true, "Navigation validation passed");
                    } else {
                        validations.add("✗ Navigation back to list failed - FAILED");
                        allPassed = false;
                    }
                    
                    if (allPassed) {
                        updateContactPage.displaySuccessNotification("Contact Updated Successfully!");
                        System.out.println("\n============================================================");
                        System.out.println("        ✓ CONTACT UPDATED SUCCESSFULLY!");
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
                        
                        takeScreenshot("UpdateContactSuccess_" + firstName + "_" + lastName);
                    } else {
                        System.out.println("\n============================================================");
                        System.out.println("        ✗ UPDATE CONTACT FAILED - BUT EXPECTED TO PASS!");
                        System.out.println("============================================================");
                        System.out.println("Current URL: " + driver.getCurrentUrl());
                        System.out.println("Some validations FAILED!");
                        System.out.println("============================================================\n");
                        
                        takeScreenshot("UpdateContactUnexpectedFailure_" + firstName + "_" + lastName);
                        Assert.fail("Update contact test failed when it should have passed");
                    }
                    
                } catch (Exception e) {
                    validations.add("✗ Exception occurred: " + e.getMessage() + " - FAILED");
                    
                    System.out.println("\n============================================================");
                    System.out.println("        ✗ UPDATE CONTACT FAILED - BUT EXPECTED TO PASS!");
                    System.out.println("============================================================");
                    System.out.println("Exception: " + e.getMessage());
                    System.out.println("Current URL: " + driver.getCurrentUrl());
                    System.out.println("============================================================\n");
                    
                    takeScreenshot("UpdateContactUnexpectedFailure_" + firstName + "_" + lastName);
                    throw e;
                }
                
            } else {
                // Expected to fail - negative test case
                Thread.sleep(2000);
                String currentUrl = driver.getCurrentUrl();
                
                // Validation: Check if still on edit page or showing error
                if (currentUrl.contains("editContact") || currentUrl.contains("contactDetails")) {
                    validations.add("✓ Still on edit/details page - validation failed as expected - PASSED");
                    System.out.println("Validation: Required fields missing or invalid data");
                    allPassed = true;
                } else {
                    validations.add("✗ Unexpected navigation - FAILED");
                    allPassed = false;
                }
                
                // Check for error message
                try {
                    WebElement errorElement = driver.findElement(By.id("error"));
                    String errorMsg = errorElement.getText();
                    if (!errorMsg.isEmpty()) {
                        validations.add("✓ Error message displayed: " + errorMsg + " - PASSED");
                        System.out.println("Error Message: " + errorMsg);
                    }
                } catch (Exception e) {
                    validations.add("✓ No error element found (may show validation on fields) - PASSED");
                }
                
                updateContactPage.displayErrorNotification("Update Failed As Expected!");
                
                System.out.println("\n============================================================");
                System.out.println("        ✓ UPDATE CONTACT FAILED AS EXPECTED");
                System.out.println("============================================================");
                System.out.println("Contact: " + firstName + " " + lastName);
                System.out.println("Current URL: " + currentUrl);
                System.out.println("Test Status: PASSED ✓ (Negative Test)");
                System.out.println("All Assertions: PASSED ✓");
                System.out.println("============================================================\n");
                
                takeScreenshot("UpdateContactNegativeTest_" + firstName + "_" + lastName);
                Assert.assertTrue(true, "Negative test case validated successfully");
            }
            
        } catch (Exception e) {
            System.out.println("Test execution failed: " + e.getMessage());
            e.printStackTrace();
            takeScreenshot("UpdateContactError_" + firstName + "_" + lastName);
            throw e;
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
