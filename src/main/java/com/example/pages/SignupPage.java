package com.example.pages;

import com.example.utils.BaseClass;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {
    
    private WebDriver driver;
    
    @FindBy(id = "firstName")
    private WebElement firstNameInput;
    
    @FindBy(id = "lastName")
    private WebElement lastNameInput;
    
    @FindBy(id = "email")
    private WebElement emailInput;
    
    @FindBy(id = "password")
    private WebElement passwordInput;
    
    @FindBy(id = "submit")
    private WebElement submitButton;
    
    @FindBy(id = "cancel")
    private WebElement cancelButton;
    
    @FindBy(id = "error")
    private WebElement errorMessage;
    
    public SignupPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    public void signup(String firstName, String lastName, String email, String password) throws InterruptedException {
        System.out.println("\n[SIGNUP] Starting signup process...");
        
        System.out.println("[ACTION] Clearing first name field...");
        BaseClass.scrollToElement(firstNameInput);
        BaseClass.highlightElement(firstNameInput, "TYPING FIRST NAME");
        firstNameInput.clear();
        System.out.println("[ACTION] Entering first name: " + firstName);
        firstNameInput.sendKeys(firstName);
        Thread.sleep(800);
        System.out.println("[SUCCESS] First name entered");
        
        System.out.println("[ACTION] Clearing last name field...");
        BaseClass.scrollToElement(lastNameInput);
        BaseClass.highlightElement(lastNameInput, "TYPING LAST NAME");
        lastNameInput.clear();
        System.out.println("[ACTION] Entering last name: " + lastName);
        lastNameInput.sendKeys(lastName);
        Thread.sleep(800);
        System.out.println("[SUCCESS] Last name entered");
        
        System.out.println("[ACTION] Clearing email field...");
        BaseClass.scrollToElement(emailInput);
        BaseClass.highlightElement(emailInput, "TYPING EMAIL");
        emailInput.clear();
        System.out.println("[ACTION] Entering email: " + email);
        emailInput.sendKeys(email);
        Thread.sleep(800);
        System.out.println("[SUCCESS] Email entered");
        
        System.out.println("[ACTION] Clearing password field...");
        BaseClass.scrollToElement(passwordInput);
        BaseClass.highlightElement(passwordInput, "TYPING PASSWORD");
        passwordInput.clear();
        System.out.println("[ACTION] Entering password: " + "*".repeat(password.length()));
        passwordInput.sendKeys(password);
        Thread.sleep(800);
        System.out.println("[SUCCESS] Password entered");
        
        System.out.println("[ACTION] Clicking submit button...");
        BaseClass.scrollToElement(submitButton);
        BaseClass.highlightElement(submitButton, "CLICKING SUBMIT");
        submitButton.click();
        System.out.println("[SUCCESS] Submit button clicked");
        Thread.sleep(2000);
        System.out.println("[SIGNUP] Signup process completed");
    }
    
    public void clickCancel() {
        cancelButton.click();
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
            "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);" +
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
            "    <div>SIGNUP SUCCESSFUL!</div>" +
            "    <div style=\"font-size: 20px; margin-top: 15px; font-weight: normal;\">Welcome, " + firstName + " " + lastName + "!</div>" +
            "';" +
            "document.body.appendChild(overlay);" +
            "var style = document.createElement('style');" +
            "style.textContent = '@keyframes slideIn { from { transform: translate(-50%, -60%); opacity: 0; } to { transform: translate(-50%, -50%); opacity: 1; } }';" +
            "document.head.appendChild(style);" +
            "setTimeout(function() { overlay.style.opacity = '0'; setTimeout(function() { overlay.remove(); }, 500); }, 5000);";
        
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
            "    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);" +
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
            "    <div>SIGNUP FAILED!</div>" +
            "    <div style=\"font-size: 18px; margin-top: 15px; font-weight: normal;\">" + errorMsg + "</div>" +
            "';" +
            "document.body.appendChild(overlay);" +
            "var style = document.createElement('style');" +
            "style.textContent = '@keyframes shake { 0%, 100% { transform: translate(-50%, -50%); } 25% { transform: translate(-48%, -50%); } 75% { transform: translate(-52%, -50%); } }';" +
            "document.head.appendChild(style);" +
            "setTimeout(function() { overlay.style.opacity = '0'; setTimeout(function() { overlay.remove(); }, 500); }, 5000);";
        
        js.executeScript(script);
    }
    
    public void displayValidationPopup(String title, String[] validations, boolean isSuccess) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Escape strings for JavaScript
            String escapedTitle = title.replace("'", "\\'").replace("\"", "\\\"");
            
            String bgColor = isSuccess ? "linear-gradient(135deg, #667eea 0%, #764ba2 100%)" : "linear-gradient(135deg, #f85032 0%, #e73827 100%)";
            String icon = isSuccess ? "&#10003;" : "&#10007;";
            
            // Build validation items JavaScript
            StringBuilder validationItems = new StringBuilder();
            for (int i = 0; i < validations.length; i++) {
                String escaped = validations[i].replace("'", "\\'").replace("\"", "\\\"");
                validationItems.append("var item").append(i).append(" = document.createElement('div');");
                validationItems.append("item").append(i).append(".style.cssText = 'text-align: left; padding: 8px 0; font-size: 16px; border-bottom: 1px solid rgba(255,255,255,0.2);';");
                validationItems.append("item").append(i).append(".innerHTML = '&#10003; ").append(escaped).append("';");
                validationItems.append("validationDiv.appendChild(item").append(i).append(");");
            }
            
            String script = 
                "var overlay = document.createElement('div');" +
                "overlay.style.cssText = 'position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); z-index: 999998; display: flex; align-items: center; justify-content: center;';" +
                "var popup = document.createElement('div');" +
                "popup.style.cssText = 'background: " + bgColor + "; color: white; padding: 40px; border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.5); max-width: 600px; max-height: 80vh; overflow-y: auto; font-family: Arial, sans-serif;';" +
                "var iconDiv = document.createElement('div');" +
                "iconDiv.style.cssText = 'font-size: 60px; text-align: center; margin-bottom: 20px;';" +
                "iconDiv.innerHTML = '" + icon + "';" +
                "var titleDiv = document.createElement('div');" +
                "titleDiv.style.cssText = 'font-size: 28px; font-weight: bold; text-align: center; margin-bottom: 30px;';" +
                "titleDiv.textContent = '" + escapedTitle + "';" +
                "var validationDiv = document.createElement('div');" +
                "validationDiv.style.cssText = 'border-top: 2px solid rgba(255,255,255,0.3); padding-top: 20px;';" +
                validationItems.toString() +
                "var closeDiv = document.createElement('div');" +
                "closeDiv.style.cssText = 'text-align: center; margin-top: 30px; font-size: 14px; opacity: 0.8;';" +
                "closeDiv.textContent = 'This popup will close in 5 seconds...';" +
                "popup.appendChild(iconDiv);" +
                "popup.appendChild(titleDiv);" +
                "popup.appendChild(validationDiv);" +
                "popup.appendChild(closeDiv);" +
                "overlay.appendChild(popup);" +
                "document.body.appendChild(overlay);" +
                "setTimeout(function() { overlay.style.opacity = '0'; setTimeout(function() { overlay.remove(); }, 300); }, 5000);";
            
            js.executeScript(script);
            Thread.sleep(1000); // Wait for animation
        } catch (Exception e) {
            System.out.println("Failed to display validation popup: " + e.getMessage());
        }
    }
}
