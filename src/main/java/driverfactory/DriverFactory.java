package driverfactory;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.GlobalVars.DEFAULT_IMPLICIT_TIMEOUTS;

public class DriverFactory {

    // Declare a ThreadLocal to hold the WebDriver instance
    private static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    private static final String CONFIG_PROPERTIES = System.getProperty("user.dir") + "/config.properties";

    // This method gets the WebDriver instance for the current thread, creating it if necessary
    public static RemoteWebDriver getWebDriver() {
        // If the ThreadLocal value is null, create a new WebDriver instance and set the ThreadLocal value
        if (driver.get() == null) {
            driver.set(createDriver());
        }
        // Return the ThreadLocal value (i.e. the WebDriver instance for the current thread)
        return driver.get();
    }

    //This method get run configuration
    private static RemoteWebDriver createDriver() {
        RemoteWebDriver driver = null;
        Capabilities  capabilities;
        //Turn off Selenium logs
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        String browserType = getConfigProperty("browserType");
        // Check the browser type and instantiate the appropriate WebDriver
        switch (browserType) {
            case "edge" -> capabilities = getEdgeCapabilities();
            case "chrome" -> capabilities = getChromeCapabilities();
            case "firefox" -> capabilities = getFirefoxCapabilities();
            case "iphonexr" -> capabilities = getMobileCapabilities("iPhone XR");
            case "samsung" -> capabilities = getMobileCapabilities("Galaxy S5");
            default -> throw new IllegalStateException("Unexpected value: " + browserType);
        }
        driver = initRemoteDriver(capabilities);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_TIMEOUTS, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

        return driver;
    }

    private static RemoteWebDriver initRemoteDriver(Capabilities capabilities) {
        RemoteWebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL(getConfigProperty("urlSeleniumHub")), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to initialize the remote driver", e);
        }
        return driver;
    }

    private static Capabilities getChromeCapabilities(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--headless");

        return chromeOptions;
    }

    private static Capabilities getEdgeCapabilities(){
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        edgeOptions.addArguments("--no-sandbox");
        edgeOptions.addArguments("--disable-dev-shm-usage");
        edgeOptions.addArguments("--headless");

        return edgeOptions;
    }

    private static Capabilities getFirefoxCapabilities(){
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        firefoxOptions.addArguments("--no-sandbox");
        firefoxOptions.addArguments("--disable-dev-shm-usage");
        firefoxOptions.addArguments("--headless");

        return firefoxOptions;
    }

    private static Capabilities getMobileCapabilities(String mobileName){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--headless");

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", mobileName);
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        chromeOptions.addArguments("--remote-allow-origins=*");
        return chromeOptions;
    }

    public static String getConfigProperty(String key) {
        String value = System.getProperty(key);

        if (value == null || value.isEmpty()) {
            try (FileInputStream file = new FileInputStream(CONFIG_PROPERTIES)) {
                Properties properties = new Properties();
                properties.load(file);
                value = properties.getProperty(key);
            } catch (IOException e) {
                System.out.println("Failed to read property " + key + " from config.properties"+ e);
            }
        }
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Failed to read property " + key + " from config.properties");
        }

        return value.toLowerCase().trim();
    }


    public static void cleanUpDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
