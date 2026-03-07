package com.example.pages;

import com.example.utils.BaseClass;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AddContactPage {
    
    private WebDriver driver;
    
    @FindBy(id = "firstName")
    private WebElement firstNameInput;
    
    @FindBy(id = "lastName")
    private WebElement lastNameInput;
    
    @FindBy(id = "birthdate")
    private WebElement birthdateInput;
    
    @FindBy(id = "email")
    private WebElement emailInput;
    
    @FindBy(id = "phone")
    private WebElement phoneInput;
    
    @FindBy(id = "street1")
    private WebElement street1Input;
    
    @FindBy(id = "street2")
    private WebElement street2Input;
    
    @FindBy(id = "city")
    private WebElement cityInput;
    
    @FindBy(id = "stateProvince")
    private WebElement stateProvinceInput;
    
    @FindBy(id = "postalCode")
    private WebElement postalCodeInput;
    
    @FindBy(id = "country")
    private WebElement countryInput;
    
    @FindBy(id = "submit")
    private WebElement submitButton;
    
    @FindBy(id = "cancel")
    private WebElement cancelButton;
    
    @FindBy(id = "error")
    private WebElement errorMessage;
    
    public AddContactPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    public void addContact(String firstName, String lastName, String birthdate, String email, 
                          String phone, String street1, String street2, String city, 
                          String stateProvince, String postalCode, String country) throws InterruptedException {
        System.out.println("\n[ADD CONTACT] Starting add contact process...");
        
        System.out.println("[ACTION] Entering first name: " + firstName);
        BaseClass.scrollToElement(firstNameInput);
        BaseClass.highlightElement(firstNameInput, "TYPING FIRST NAME");
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering last name: " + lastName);
        BaseClass.scrollToElement(lastNameInput);
        BaseClass.highlightElement(lastNameInput, "TYPING LAST NAME");
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering birthdate: " + birthdate);
        BaseClass.scrollToElement(birthdateInput);
        BaseClass.highlightElement(birthdateInput, "TYPING BIRTHDATE");
        birthdateInput.clear();
        birthdateInput.sendKeys(birthdate);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering email: " + email);
        BaseClass.scrollToElement(emailInput);
        BaseClass.highlightElement(emailInput, "TYPING EMAIL");
        emailInput.clear();
        emailInput.sendKeys(email);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering phone: " + phone);
        BaseClass.scrollToElement(phoneInput);
        BaseClass.highlightElement(phoneInput, "TYPING PHONE");
        phoneInput.clear();
        phoneInput.sendKeys(phone);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering street1: " + street1);
        BaseClass.scrollToElement(street1Input);
        BaseClass.highlightElement(street1Input, "TYPING STREET1");
        street1Input.clear();
        street1Input.sendKeys(street1);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering street2: " + street2);
        BaseClass.scrollToElement(street2Input);
        BaseClass.highlightElement(street2Input, "TYPING STREET2");
        street2Input.clear();
        street2Input.sendKeys(street2);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering city: " + city);
        BaseClass.scrollToElement(cityInput);
        BaseClass.highlightElement(cityInput, "TYPING CITY");
        cityInput.clear();
        cityInput.sendKeys(city);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering state/province: " + stateProvince);
        BaseClass.scrollToElement(stateProvinceInput);
        BaseClass.highlightElement(stateProvinceInput, "TYPING STATE");
        stateProvinceInput.clear();
        stateProvinceInput.sendKeys(stateProvince);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering postal code: " + postalCode);
        BaseClass.scrollToElement(postalCodeInput);
        BaseClass.highlightElement(postalCodeInput, "TYPING POSTAL CODE");
        postalCodeInput.clear();
        postalCodeInput.sendKeys(postalCode);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Entering country: " + country);
        BaseClass.scrollToElement(countryInput);
        BaseClass.highlightElement(countryInput, "TYPING COUNTRY");
        countryInput.clear();
        countryInput.sendKeys(country);
        Thread.sleep(800);
        
        System.out.println("[ACTION] Clicking submit button...");
        BaseClass.scrollToElement(submitButton);
        BaseClass.highlightElement(submitButton, "CLICKING SUBMIT");
        submitButton.click();
        System.out.println("[SUCCESS] Contact form submitted");
        Thread.sleep(2000);
        System.out.println("[ADD CONTACT] Add contact process completed");
    }
    
    public String getErrorMessage() {
        try {
            if (errorMessage.isDisplayed()) {
                return errorMessage.getText();
            }
        } catch (Exception e) {
            // Error message not found
        }
        return "";
    }
    
    public void displaySuccessNotification(String firstName, String lastName) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        String script = 
            "var overlay = document.createElement('div');" +
            "overlay.id = 'successOverlay';" +
            "overlay.style.cssText = '" +
            "    position: fixed;" +
            "    top: 50%;" +
            "    left: 50%;" +
            "    transform: translate(-50%, -50%);" +
            "    background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);" +
            "    color: white;" +
            "    padding: 50px 60px;" +
            "    border-radius: 20px;" +
            "    box-shadow: 0 20px 60px rgba(0,0,0,0.3);" +
            "    font-family: Arial, sans-serif;" +
            "    font-size: 28px;" +
            "    font-weight: bold;" +
            "    z-index: 10000;" +
            "    text-align: center;" +
            "    animation: slideIn 0.5s ease-out;" +
            "';" +
            "overlay.innerHTML = '" +
            "    <div style=\"font-size: 60px; margin-bottom: 20px;\">✓</div>" +
            "    <div>CONTACT ADDED SUCCESSFULLY!</div>" +
            "    <div style=\"font-size: 20px; margin-top: 15px; font-weight: normal;\">Contact: " + firstName + " " + lastName + "</div>" +
            "';" +
            "document.body.appendChild(overlay);" +
            "setTimeout(function() { overlay.remove(); }, 5000);";
        
        js.executeScript(script);
    }
    
    public void displayErrorNotification(String errorMsg) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        String script = 
            "var overlay = document.createElement('div');" +
            "overlay.id = 'errorOverlay';" +
            "overlay.style.cssText = '" +
            "    position: fixed;" +
            "    top: 50%;" +
            "    left: 50%;" +
            "    transform: translate(-50%, -50%);" +
            "    background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%);" +
            "    color: white;" +
            "    padding: 50px 60px;" +
            "    border-radius: 20px;" +
            "    box-shadow: 0 20px 60px rgba(0,0,0,0.3);" +
            "    font-family: Arial, sans-serif;" +
            "    font-size: 28px;" +
            "    font-weight: bold;" +
            "    z-index: 10000;" +
            "    text-align: center;" +
            "    animation: shake 0.5s ease-out;" +
            "';" +
            "overlay.innerHTML = '" +
            "    <div style=\"font-size: 60px; margin-bottom: 20px;\">✗</div>" +
            "    <div>FAILED TO ADD CONTACT!</div>" +
            "    <div style=\"font-size: 18px; margin-top: 15px; font-weight: normal;\">" + errorMsg.replace("'", "\\'") + "</div>" +
            "';" +
            "document.body.appendChild(overlay);" +
            "setTimeout(function() { overlay.remove(); }, 5000);";
        
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
