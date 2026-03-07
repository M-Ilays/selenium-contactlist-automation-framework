package com.example.pages;

import com.example.utils.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class LoginPage {

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(linkText = "Click here to sign up!")
    private WebElement signUpLink;

    // Success message or notification after login
    @FindBy(xpath = "//h1[contains(text(),'Contact List')] | //span[@class='notification'] | //*[contains(text(),'Login successful')]")
    private WebElement loginSuccessIndicator;

    // Error message locator
    @FindBy(id = "error")
    private WebElement errorMessage;

    public LoginPage() {
        PageFactory.initElements(BaseClass.driver, this);
    }

    public void enterEmail(String email) throws InterruptedException {
        System.out.println("[ACTION] Clearing email field...");
        BaseClass.scrollToElement(emailField);
        BaseClass.highlightElement(emailField, "TYPING EMAIL");
        emailField.clear();
        System.out.println("[ACTION] Entering email: " + email);
        emailField.sendKeys(email);
        Thread.sleep(800);
        System.out.println("[SUCCESS] Email entered successfully");
    }

    public void enterPassword(String password) throws InterruptedException {
        System.out.println("[ACTION] Clearing password field...");
        BaseClass.scrollToElement(passwordField);
        BaseClass.highlightElement(passwordField, "TYPING PASSWORD");
        passwordField.clear();
        System.out.println("[ACTION] Entering password: " + "*".repeat(password.length()));
        passwordField.sendKeys(password);
        Thread.sleep(800);
        System.out.println("[SUCCESS] Password entered successfully");
    }

    public void clickSubmit() throws InterruptedException {
        Thread.sleep(500);
        System.out.println("[ACTION] Clicking submit button...");
        BaseClass.scrollToElement(submitButton);
        BaseClass.highlightElement(submitButton, "CLICKING SUBMIT");
        submitButton.click();
        System.out.println("[SUCCESS] Submit button clicked");
    }

    public void clickSignUpLink() {
        System.out.println("[ACTION] Clicking sign up link...");
        signUpLink.click();
        System.out.println("[SUCCESS] Sign up link clicked");
    }

    public void login(String email, String password) throws InterruptedException {
        System.out.println("\n[LOGIN] Starting login process...");
        enterEmail(email);
        enterPassword(password);
        clickSubmit();
    }

    public String getPageTitle() {
        return BaseClass.driver.getTitle();
    }

    public String getSuccessMessage() {
        try {
            return loginSuccessIndicator.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isLoginSuccessful() {
        try {
            // Check if URL contains contactList or if we're no longer on login page
            return BaseClass.driver.getCurrentUrl().contains("contactList") ||
                   !BaseClass.driver.getCurrentUrl().contains("login");
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageNotification() {
        try {
            // Try multiple common notification selectors
            String[] selectors = {
                "#error",
                ".notification",
                ".alert",
                ".message",
                "[role='alert']",
                ".toast",
                "#notification"
            };
            
            for (String selector : selectors) {
                try {
                    List<WebElement> elements = BaseClass.driver.findElements(By.cssSelector(selector));
                    if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                        return elements.get(0).getText();
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            return "No notification message found on page";
        } catch (Exception e) {
            return "Error retrieving notification: " + e.getMessage();
        }
    }

    public String getErrorMessage() {
        try {
            // Try multiple error message selectors
            String[] selectors = {
                "#error",
                ".error",
                ".error-message",
                "[role='alert']",
                ".alert-danger",
                "span[style*='color: red']",
                "div[style*='color: red']"
            };
            
            for (String selector : selectors) {
                try {
                    List<WebElement> elements = BaseClass.driver.findElements(By.cssSelector(selector));
                    if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                        String text = elements.get(0).getText();
                        if (!text.isEmpty()) {
                            return text;
                        }
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            return "Invalid credentials or error message not displayed";
        } catch (Exception e) {
            return "Error retrieving error message: " + e.getMessage();
        }
    }

    public void displaySuccessNotification(String message) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) BaseClass.driver;
            String script = 
                "var notification = document.createElement('div');" +
                "notification.id = 'customSuccessNotification';" +
                "notification.innerHTML = '<div style=\"" +
                "position: fixed;" +
                "top: 20px;" +
                "left: 50%;" +
                "transform: translateX(-50%);" +
                "background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);" +
                "color: white;" +
                "padding: 20px 40px;" +
                "border-radius: 12px;" +
                "font-size: 24px;" +
                "font-weight: bold;" +
                "font-family: Arial, sans-serif;" +
                "box-shadow: 0 10px 40px rgba(0,0,0,0.3);" +
                "z-index: 999999;" +
                "text-align: center;" +
                "animation: slideDown 0.5s ease-out;" +
                "border: 3px solid #fff;" +
                "\">" +
                "<div style=\"font-size: 40px; margin-bottom: 10px;\">✓</div>" +
                "<div>" + message + "</div>" +
                "</div>';" +
                "var style = document.createElement('style');" +
                "style.textContent = '@keyframes slideDown { from { top: -100px; opacity: 0; } to { top: 20px; opacity: 1; } }';" +
                "document.head.appendChild(style);" +
                "document.body.appendChild(notification);";
            js.executeScript(script);
            Thread.sleep(1000); // Wait for animation
        } catch (Exception e) {
            System.out.println("Failed to display notification: " + e.getMessage());
        }
    }

    public void displayErrorNotification(String message) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) BaseClass.driver;
            String script = 
                "var notification = document.createElement('div');" +
                "notification.id = 'customErrorNotification';" +
                "notification.innerHTML = '<div style=\"" +
                "position: fixed;" +
                "top: 20px;" +
                "left: 50%;" +
                "transform: translateX(-50%);" +
                "background: linear-gradient(135deg, #f85032 0%, #e73827 100%);" +
                "color: white;" +
                "padding: 20px 40px;" +
                "border-radius: 12px;" +
                "font-size: 24px;" +
                "font-weight: bold;" +
                "font-family: Arial, sans-serif;" +
                "box-shadow: 0 10px 40px rgba(0,0,0,0.3);" +
                "z-index: 999999;" +
                "text-align: center;" +
                "animation: slideDown 0.5s ease-out;" +
                "border: 3px solid #fff;" +
                "\">" +
                "<div style=\"font-size: 40px; margin-bottom: 10px;\">✗</div>" +
                "<div>" + message + "</div>" +
                "</div>';" +
                "var style = document.createElement('style');" +
                "style.textContent = '@keyframes slideDown { from { top: -100px; opacity: 0; } to { top: 20px; opacity: 1; } }';" +
                "document.head.appendChild(style);" +
                "document.body.appendChild(notification);";
            js.executeScript(script);
            Thread.sleep(1000); // Wait for animation
        } catch (Exception e) {
            System.out.println("Failed to display error notification: " + e.getMessage());
        }
    }
    
    public void displayValidationPopup(String title, String[] validations, boolean isSuccess) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) BaseClass.driver;
            
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