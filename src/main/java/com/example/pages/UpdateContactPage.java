package com.example.pages;

import com.example.utils.BaseClass;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UpdateContactPage {
    
    WebDriver driver;
    
    @FindBy(id = "firstName")
    WebElement firstName;
    
    @FindBy(id = "lastName")
    WebElement lastName;
    
    @FindBy(id = "birthdate")
    WebElement birthdate;
    
    @FindBy(id = "email")
    WebElement email;
    
    @FindBy(id = "phone")
    WebElement phone;
    
    @FindBy(id = "street1")
    WebElement street1;
    
    @FindBy(id = "street2")
    WebElement street2;
    
    @FindBy(id = "city")
    WebElement city;
    
    @FindBy(id = "stateProvince")
    WebElement stateProvince;
    
    @FindBy(id = "postalCode")
    WebElement postalCode;
    
    @FindBy(id = "country")
    WebElement country;
    
    @FindBy(id = "submit")
    WebElement submitButton;
    
    @FindBy(id = "cancel")
    WebElement cancelButton;
    
    public UpdateContactPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    public void updateContact(String fName, String lName, String bdate, String mail, 
                             String phoneNum, String str1, String str2, String cty, 
                             String state, String postal, String ctry) throws InterruptedException {
        
        System.out.println("\n[UPDATE CONTACT] Starting update contact process...");
        
        System.out.println("[ACTION] Updating first name to: " + fName);
        BaseClass.scrollToElement(firstName);
        BaseClass.highlightElement(firstName, "UPDATING FIRST NAME");
        firstName.clear();
        Thread.sleep(800);
        firstName.sendKeys(fName);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating last name to: " + lName);
        BaseClass.scrollToElement(lastName);
        BaseClass.highlightElement(lastName, "UPDATING LAST NAME");
        lastName.clear();
        Thread.sleep(800);
        lastName.sendKeys(lName);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating birthdate to: " + bdate);
        BaseClass.scrollToElement(birthdate);
        BaseClass.highlightElement(birthdate, "UPDATING BIRTHDATE");
        birthdate.clear();
        Thread.sleep(800);
        birthdate.sendKeys(bdate);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating email to: " + mail);
        BaseClass.scrollToElement(email);
        BaseClass.highlightElement(email, "UPDATING EMAIL");
        email.clear();
        Thread.sleep(800);
        email.sendKeys(mail);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating phone to: " + phoneNum);
        BaseClass.scrollToElement(phone);
        BaseClass.highlightElement(phone, "UPDATING PHONE");
        phone.clear();
        Thread.sleep(800);
        phone.sendKeys(phoneNum);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating street1 to: " + str1);
        BaseClass.scrollToElement(street1);
        BaseClass.highlightElement(street1, "UPDATING STREET1");
        street1.clear();
        Thread.sleep(800);
        street1.sendKeys(str1);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating street2 to: " + str2);
        BaseClass.scrollToElement(street2);
        BaseClass.highlightElement(street2, "UPDATING STREET2");
        street2.clear();
        Thread.sleep(800);
        street2.sendKeys(str2);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating city to: " + cty);
        BaseClass.scrollToElement(city);
        BaseClass.highlightElement(city, "UPDATING CITY");
        city.clear();
        Thread.sleep(800);
        city.sendKeys(cty);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating state/province to: " + state);
        BaseClass.scrollToElement(stateProvince);
        BaseClass.highlightElement(stateProvince, "UPDATING STATE");
        stateProvince.clear();
        Thread.sleep(800);
        stateProvince.sendKeys(state);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating postal code to: " + postal);
        BaseClass.scrollToElement(postalCode);
        BaseClass.highlightElement(postalCode, "UPDATING POSTAL CODE");
        postalCode.clear();
        Thread.sleep(800);
        postalCode.sendKeys(postal);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Updating country to: " + ctry);
        BaseClass.scrollToElement(country);
        BaseClass.highlightElement(country, "UPDATING COUNTRY");
        country.clear();
        Thread.sleep(800);
        country.sendKeys(ctry);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Clicking submit button...");
        BaseClass.scrollToElement(submitButton);
        BaseClass.highlightElement(submitButton, "CLICKING SUBMIT");
        submitButton.click();
        System.out.println("[SUCCESS] Update form submitted");
        Thread.sleep(2000);
        System.out.println("[UPDATE CONTACT] Update contact process completed");
    }
    
    public void displaySuccessNotification(String message) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "var notification = document.createElement('div');" +
                       "notification.style.cssText = 'position: fixed; top: 20px; right: 20px; " +
                       "background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; " +
                       "padding: 20px 30px; border-radius: 10px; box-shadow: 0 10px 40px rgba(0,0,0,0.3); " +
                       "z-index: 10000; font-size: 18px; font-weight: bold; animation: slideIn 0.5s ease-out;';" +
                       "notification.innerHTML = '<span style=\"font-size: 30px; margin-right: 15px;\">&#10003;</span>" + message + "';" +
                       "document.body.appendChild(notification);" +
                       "var style = document.createElement('style');" +
                       "style.innerHTML = '@keyframes slideIn { from { transform: translateX(400px); opacity: 0; } " +
                       "to { transform: translateX(0); opacity: 1; } }';" +
                       "document.head.appendChild(style);" +
                       "setTimeout(function() { notification.remove(); }, 3000);";
        js.executeScript(script);
    }
    
    public void displayErrorNotification(String message) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "var notification = document.createElement('div');" +
                       "notification.style.cssText = 'position: fixed; top: 20px; right: 20px; " +
                       "background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); color: white; " +
                       "padding: 20px 30px; border-radius: 10px; box-shadow: 0 10px 40px rgba(0,0,0,0.3); " +
                       "z-index: 10000; font-size: 18px; font-weight: bold; animation: shake 0.5s ease-out;';" +
                       "notification.innerHTML = '<span style=\"font-size: 30px; margin-right: 15px;\">&#10007;</span>" + message + "';" +
                       "document.body.appendChild(notification);" +
                       "var style = document.createElement('style');" +
                       "style.innerHTML = '@keyframes shake { 0%, 100% { transform: translateX(0); } " +
                       "25% { transform: translateX(-10px); } 75% { transform: translateX(10px); } }';" +
                       "document.head.appendChild(style);" +
                       "setTimeout(function() { notification.remove(); }, 3000);";
        js.executeScript(script);
    }
    
    public void displayValidationPopup(String title, String[] validations, boolean isSuccess) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        String bgColor = isSuccess ? "#667eea" : "#eb3349";
        String icon = isSuccess ? "&#10003;" : "&#10007;";
        
        // Build validation list using createElement
        String script = "var overlay = document.createElement('div');" +
                        "overlay.style.cssText = 'position: fixed; top: 0; left: 0; width: 100%; height: 100%; " +
                        "background: rgba(0,0,0,0.7); z-index: 9999; display: flex; align-items: center; justify-content: center;';" +
                        "var popup = document.createElement('div');" +
                        "popup.style.cssText = 'background: linear-gradient(135deg, " + bgColor + " 0%, " + bgColor + "dd 100%); " +
                        "color: white; padding: 40px; border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.5); " +
                        "max-width: 600px; width: 90%; font-family: Arial, sans-serif;';" +
                        "var iconDiv = document.createElement('div');" +
                        "iconDiv.style.cssText = 'font-size: 60px; text-align: center; margin-bottom: 20px;';" +
                        "iconDiv.innerHTML = '" + icon + "';" +
                        "var titleDiv = document.createElement('div');" +
                        "titleDiv.style.cssText = 'font-size: 24px; font-weight: bold; text-align: center; margin-bottom: 25px;';" +
                        "titleDiv.textContent = " + escapeForJS(title) + ";" +
                        "var validationDiv = document.createElement('div');" +
                        "validationDiv.style.cssText = 'font-size: 16px; max-height: 400px; overflow-y: auto;';";
        
        // Add each validation as a separate div element
        for (String validation : validations) {
            script += "var item" + Math.abs(validation.hashCode()) + " = document.createElement('div');" +
                     "item" + Math.abs(validation.hashCode()) + ".style.cssText = 'padding: 8px 0; border-bottom: 1px solid rgba(255,255,255,0.2);';" +
                     "item" + Math.abs(validation.hashCode()) + ".textContent = " + escapeForJS(validation) + ";" +
                     "validationDiv.appendChild(item" + Math.abs(validation.hashCode()) + ");";
        }
        
        script += "var closeDiv = document.createElement('div');" +
                 "closeDiv.style.cssText = 'text-align: center; margin-top: 25px; font-size: 14px; opacity: 0.8;';" +
                 "closeDiv.textContent = 'Auto-closing in 5 seconds...';" +
                 "popup.appendChild(iconDiv);" +
                 "popup.appendChild(titleDiv);" +
                 "popup.appendChild(validationDiv);" +
                 "popup.appendChild(closeDiv);" +
                 "overlay.appendChild(popup);" +
                 "document.body.appendChild(overlay);" +
                 "setTimeout(function() { overlay.remove(); }, 5000);";
        
        js.executeScript(script);
    }
    
    private String escapeForJS(String text) {
        // Escape single quotes and backslashes, then wrap in quotes
        String escaped = text.replace("\\", "\\\\").replace("'", "\\'");
        return "'" + escaped + "'";
    }
}
