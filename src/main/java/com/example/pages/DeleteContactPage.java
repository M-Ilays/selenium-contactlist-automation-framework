package com.example.pages;

import com.example.utils.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DeleteContactPage {
    
    private WebDriver driver;
    
    @FindBy(id = "myTable")
    private WebElement contactTable;
    
    @FindBy(id = "delete")
    private WebElement deleteButton;
    
    public DeleteContactPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    public void clickContactByName(String firstName, String lastName) throws InterruptedException {
        Thread.sleep(800);
        String fullName = (firstName + " " + lastName).trim();
        
        System.out.println("\n[DELETE CONTACT] Searching for contact: " + fullName);
        
        // Find the contact row by name
        List<WebElement> rows = driver.findElements(By.cssSelector("#myTable tr"));
        System.out.println("[INFO] Found " + rows.size() + " rows in contact table");
        
        for (WebElement row : rows) {
            if (row.getText().contains(fullName)) {
                System.out.println("[ACTION] Clicking on contact: " + fullName);
                BaseClass.scrollToElement(row);
                BaseClass.highlightElement(row, "CLICKING CONTACT");
                row.click();
                System.out.println("[SUCCESS] Contact clicked: " + fullName);
                Thread.sleep(1500);
                return;
            }
        }
        
        System.out.println("[WARNING] Contact not found: " + fullName);
    }
    
    public void deleteContact() throws InterruptedException {
        Thread.sleep(800);
        System.out.println("[ACTION] Clicking delete button...");
        BaseClass.scrollToElement(deleteButton);
        BaseClass.highlightElement(deleteButton, "CLICKING DELETE");
        deleteButton.click();
        System.out.println("[SUCCESS] Delete button clicked");
        
        // Handle alert confirmation
        Thread.sleep(500);
        try {
            System.out.println("[ACTION] Accepting delete confirmation alert...");
            driver.switchTo().alert().accept();
            System.out.println("[SUCCESS] Delete confirmation accepted");
        } catch (Exception e) {
            System.out.println("[WARNING] No alert present");
        }
        
        Thread.sleep(2000);
        System.out.println("[DELETE CONTACT] Delete operation completed");
    }
    
    public void displaySuccessNotification(String contactName) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        String script = "var notification = document.createElement('div');" +
                       "notification.style.cssText = 'position: fixed; top: 20px; right: 20px; " +
                       "background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; " +
                       "padding: 20px 30px; border-radius: 10px; box-shadow: 0 10px 40px rgba(0,0,0,0.3); " +
                       "z-index: 10000; font-family: Arial; font-size: 16px; " +
                       "animation: slideIn 0.5s ease-out;';" +
                       "notification.innerHTML = '<div style=\"font-size: 40px; text-align: center;\">&#128465;</div>" +
                       "<div style=\"font-weight: bold; margin-top: 10px;\">Contact Deleted Successfully!</div>" +
                       "<div style=\"margin-top: 5px; font-size: 14px;\">" + contactName + "</div>';" +
                       "document.body.appendChild(notification);" +
                       "setTimeout(function() { notification.remove(); }, 3000);";
        
        js.executeScript(script);
    }
    
    public void displayErrorNotification(String message) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        String script = "var notification = document.createElement('div');" +
                       "notification.style.cssText = 'position: fixed; top: 20px; right: 20px; " +
                       "background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); color: white; " +
                       "padding: 20px 30px; border-radius: 10px; box-shadow: 0 10px 40px rgba(0,0,0,0.3); " +
                       "z-index: 10000; font-family: Arial; font-size: 16px; " +
                       "animation: shake 0.5s ease-out;';" +
                       "notification.innerHTML = '<div style=\"font-size: 40px; text-align: center;\">&#10007;</div>" +
                       "<div style=\"font-weight: bold; margin-top: 10px;\">Delete Failed!</div>" +
                       "<div style=\"margin-top: 5px; font-size: 14px;\">" + message + "</div>';" +
                       "document.body.appendChild(notification);" +
                       "setTimeout(function() { notification.remove(); }, 3000);";
        
        js.executeScript(script);
    }
    
    public void displayValidationPopup(String title, String[] validations, boolean isSuccess) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        String bgColor = isSuccess ? "#667eea" : "#eb3349";
        String icon = isSuccess ? "&#10003;" : "&#10007;";
        
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
        if (text == null) {
            return "''";
        }
        // Properly escape all special characters for JavaScript string literals
        String escaped = text
            .replace("\\", "\\\\")      // Escape backslashes first
            .replace("\r", "\\r")        // Escape carriage returns
            .replace("\n", "\\n")        // Escape newlines
            .replace("\t", "\\t")        // Escape tabs
            .replace("'", "\\'")         // Escape single quotes
            .replace("\"", "\\\"")       // Escape double quotes
            .replace("/", "\\/");        // Escape forward slashes
        return "'" + escaped + "'";
    }
}
