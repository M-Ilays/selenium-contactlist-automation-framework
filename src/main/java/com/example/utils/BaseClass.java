package com.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class BaseClass {
    public static WebDriver driver;
    public static Properties prop;

    static {
        prop = new Properties();
        try {
            InputStream fis = BaseClass.class.getClassLoader().getResourceAsStream("config.properties");
            if (fis != null) {
                prop.load(fis);
            } else {
                throw new IOException("config.properties not found in classpath");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeDriver() {
        String browser = prop.getProperty("browser");

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                driver = new EdgeDriver(edgeOptions);
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(prop.getProperty("implicitWait"))));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Integer.parseInt(prop.getProperty("pageLoadTimeout"))));
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
    
    /**
     * Highlights element with visual animation before interaction
     * @param element WebElement to highlight
     * @param action Action being performed (e.g., "TYPING", "CLICKING")
     */
    public static void highlightElement(WebElement element, String action) {
        if (driver == null) return;
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Store original style
        String originalStyle = element.getAttribute("style");
        
        // Highlight animation with different colors based on action
        String color = action.contains("CLICK") ? "#FF6B35" : "#4ECDC4";
        
        String highlightStyle = 
            "border: 3px solid " + color + " !important; " +
            "background-color: rgba(255, 235, 59, 0.3) !important; " +
            "box-shadow: 0 0 20px " + color + " !important; " +
            "transition: all 0.3s ease !important;";
        
        try {
            // Apply highlight
            js.executeScript(
                "arguments[0].setAttribute('style', arguments[1]);",
                element, highlightStyle
            );
            
            // Add action indicator
            js.executeScript(
                "var indicator = document.createElement('div');" +
                "indicator.textContent = '" + action + "';" +
                "indicator.style.cssText = '" +
                "position: fixed; " +
                "top: 10px; " +
                "right: 10px; " +
                "background: " + color + "; " +
                "color: white; " +
                "padding: 10px 20px; " +
                "border-radius: 5px; " +
                "font-weight: bold; " +
                "z-index: 999999; " +
                "animation: fadeInOut 0.8s ease-in-out;';" +
                "document.body.appendChild(indicator);" +
                "setTimeout(function() { indicator.remove(); }, 800);"
            );
            
            // Add CSS animation
            js.executeScript(
                "if (!document.getElementById('actionAnimationStyle')) {" +
                "  var style = document.createElement('style');" +
                "  style.id = 'actionAnimationStyle';" +
                "  style.innerHTML = '@keyframes fadeInOut { " +
                "    0% { opacity: 0; transform: translateX(50px); } " +
                "    50% { opacity: 1; transform: translateX(0); } " +
                "    100% { opacity: 0; transform: translateX(-50px); } " +
                "  }';" +
                "  document.head.appendChild(style);" +
                "}"
            );
            
            Thread.sleep(400);
            
            // Restore original style
            js.executeScript(
                "arguments[0].setAttribute('style', arguments[1]);",
                element, originalStyle != null ? originalStyle : ""
            );
            
        } catch (Exception e) {
            // Silently continue if highlight fails
        }
    }
    
    /**
     * Scroll element into view with smooth animation
     */
    public static void scrollToElement(WebElement element) {
        if (driver == null) return;
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            js.executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'});",
                element
            );
            Thread.sleep(300);
        } catch (Exception e) {
            // Silently continue
        }
    }
}