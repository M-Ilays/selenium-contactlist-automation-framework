package com.example.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing Allure reporting features
 */
public class AllureManager {
    
    private static final Map<String, String> environmentInfo = new HashMap<>();
    
    /**
     * Attach a screenshot to the Allure report
     * @param driver WebDriver instance
     */
    public static void takeScreenshot(WebDriver driver) {
        if (driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot", "image/png", 
                    new ByteArrayInputStream(screenshot), ".png");
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }
    
    /**
     * Set environment information for Allure report
     * @param key Environment property key
     * @param value Environment property value
     */
    public static void setEnvironmentInfo(String key, String value) {
        environmentInfo.put(key, value);
    }
    
    /**
     * Write environment information to the allure-results directory
     */
    public static void writeEnvironmentInfo() {
        try {
            String allureResultsPath = "target/allure-results/environment.properties";
            try (FileWriter writer = new FileWriter(allureResultsPath)) {
                for (Map.Entry<String, String> entry : environmentInfo.entrySet()) {
                    writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to write environment info: " + e.getMessage());
        }
    }
}
