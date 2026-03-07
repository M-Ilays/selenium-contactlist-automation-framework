package com.example.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

/**
 * Utility class for Allure report enhancements
 */
public class AllureManager {

    /**
     * Attach screenshot to Allure report
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            if (driver != null) {
                return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            }
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot for Allure: " + e.getMessage());
        }
        return new byte[0];
    }

    /**
     * Attach screenshot with custom name
     */
    public static void attachScreenshot(WebDriver driver, String name) {
        try {
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
            }
        } catch (Exception e) {
            System.out.println("Failed to attach screenshot: " + e.getMessage());
        }
    }

    /**
     * Attach text to Allure report
     */
    @Attachment(value = "{attachmentName}", type = "text/plain")
    public static String attachText(String attachmentName, String text) {
        return text;
    }

    /**
     * Attach HTML to Allure report
     */
    @Attachment(value = "{attachmentName}", type = "text/html")
    public static String attachHtml(String attachmentName, String html) {
        return html;
    }

    /**
     * Log step to Allure report
     */
    public static void logStep(String stepDescription) {
        Allure.step(stepDescription);
    }

    /**
     * Add environment information to Allure report
     */
    public static void setEnvironmentInfo(String name, String value) {
        Allure.addAttachment(name, "text/plain", value, ".txt");
    }
}
