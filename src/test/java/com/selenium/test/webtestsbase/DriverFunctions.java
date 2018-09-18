package com.selenium.test.webtestsbase;

//import static qa.automation.support.CommonFunctions.*;
//import static qa.automation.support.MavenFunctions.*;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Assertions;
//import org.testng.Assert;

/**
 * Various Selenium-specific utility methods for the QA automated functional testing suite.
 */
public class DriverFunctions {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverFunctions.class.getName());
    private static String browser = "";

    /**
     * This can be used to create the WebDriver object for a particular browser. Example: 
     * driver = DriverFunctions.openBrowser(browser); 
     * In the above case, the var browser is a parameter value passed from the XML file.
     * @param browser Takes in a string to specify which browser to use.
     * @return driver The WebDriver object for the specified browser.
     */
    public static WebDriver openBrowser(String browser) {

            if (browser.equalsIgnoreCase("chrome")) {
                setBrowser("Chrome");
                ChromeDriverManager.getInstance().setup();

                // If cmd line parameter for echo.headless = true, then set the headless option. Else, use full UI.
                if (getHeadless()) {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless");
                    return new ChromeDriver(chromeOptions);
                } else {
                    return new ChromeDriver();
                }

            } else if (browser.equalsIgnoreCase("ff") || browser.equalsIgnoreCase("firefox")) {
                setBrowser("Firefox");
                FirefoxDriverManager.getInstance().setup();

                // If cmd line parameter for echo.headless = true, then set the headless option. Else, use full UI.
                if (getHeadless()) {
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless");
                    return new FirefoxDriver(firefoxOptions);
                } else {
                    return new FirefoxDriver();
                }

            } else if (browser.equalsIgnoreCase("edge")) {
                setBrowser("Edge");
                EdgeDriverManager.getInstance().setup();
                return new EdgeDriver();

            } else {
                Assert.fail("Couldn't find the appropriate browser.");
                return null;
            }
        }

    }

    /**
     * To find out if an image has loaded successfully, use this method. EXAMPLE: 
     * DriverFunctions.checkImage(driver, imageBlogPosts(driver), "Blog Posts");
     * @param driver The Selenium WebDriver object created for the test.
     * @param element The image element to test.
     * @param imageName The name of the image to test. This is a made up descriptive name.
     */
    public static void checkImage(WebDriver driver, WebElement element, String imageName) {

        // If true, the image successfully loaded. If false, it did not load.
        Boolean imagePresent = (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].complete && "
                + "typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", element);

        // Log a message to show a pass. Stop the test with an assertion failure if not.
        if (imagePresent) {
            logPassStep("The " + imageName + " image loaded successfully.");
        } else {
            Assert.fail("The " + imageName + " image failed to load.");
        }

    }

    /**
     * To find out if the appropriate values are found for headings, use this method. EXAMPLE:
     * DriverFunctions.checkHeading(element, expectedText, expectedColor, expectedFontWeight);
     * @param element The heading element to test.
     * @param expectedText The expected text of the heading.
     * @param expectedColor The expected color of the heading. Format: rgba(51, 102, 204, 1)
     * @param expectedFontWeight The expected font weight of the heading. Example: bold
     */
    public static void checkHeading(WebElement element, String expectedText, String expectedColor,
            String expectedFontWeight) {

        String actualText = element.getAttribute("textContent");
        String actualColor = element.getCssValue("color");
        String actualFontWeight = element.getCssValue("font-weight");

        if (actualText.contains(expectedText) && actualColor.contains(expectedColor) && actualFontWeight.contains(
                expectedFontWeight)) {
            logPassStep("The " + expectedText + " heading was found with all of the " + "appropriate attributes.");
        } else {
            Assert.fail("At least one of the expected attributes of the " + expectedText + " heading was not found:"
                    + "\nExpected Text: '" + expectedText + "' vs Actual Text: '" + actualText + "'"
                    + "\nExpected Color: '" + expectedColor + "' vs Actual Color: '" + actualColor + "'"
                    + "\nExpected Font Weight: '" + expectedFontWeight + "' vs Actual Font Weight: '" + actualFontWeight
                    + "'");
        }

    }

    /**
     * To find out if the text content is as expected, use this method. EXAMPLE: 
     * DriverFunctions.checkHeading(element, "Check Configuration", "Check Configuration button");
     * @param element The element to test.
     * @param expectedText The expected text of the element.
     * @param elementName The name of the element to test. This is a made up descriptive name.
     */
    public static void checkText(WebElement element, String expectedText, String elementName) {
        String actualText = element.getAttribute("textContent");
        if (actualText.contains(expectedText)) {
            logPassStep("The expected text was found for the " + elementName + ": '" + expectedText + "'.");
        } else {
            Assert.fail("The expected text was not found for the " + elementName + ". Found '" + actualText
                    + "' but was expecting it to contain '" + expectedText + "'.");
        }
    }

    /**
     * Sets an option from a dropdown select menu using the selectByVisibleText method. EXAMPLE:
     * DriverFunctions.setSelectOptionByVisibleText(PageObjectsHome.selectItemPool(driver),"alextesting","Item Pool");
     * @param element The element to test.
     * @param input The actual text used to select the option in the dropdown menu.
     * @param fieldName The name of the element to test. This is a made up descriptive name.
     * @throws InterruptedException Needed for sleep.
     */
    public static void setSelectOptionByVisibleText(WebElement element, String input, String fieldName)
            throws InterruptedException {
        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(input);
        logInfo("Visible text '" + input + "' was chosen from the '" + fieldName + "' dropdown menu.");
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * Sets an option from a dropdown select menu using the selectByIndex method. EXAMPLE:
     * DriverFunctions.setSelectOptionByIndex(PageObjectsHome.selectItemPool(driver), 5, "Item Pool");
     * @param element The element to test.
     * @param input The index position used to select the option in the dropdown menu. (Starting with 0)
     * @param fieldName The name of the element to test. This is a made up descriptive name.
     * @throws InterruptedException Needed for sleep.
     */
    public static void setSelectOptionByIndex(WebElement element, int input, String fieldName)
            throws InterruptedException {
        Select dropdown = new Select(element);
        dropdown.selectByIndex(input);
        logInfo("Index '" + input + "' was chosen from the '" + fieldName + "' dropdown menu.");
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * Sets an option from a dropdown select menu using the selectByValue method. EXAMPLE:
     * DriverFunctions.setSelectOptionByValue(PageObjectsHome.selectItemPool(driver), "6", "Item Pool");
     * @param element The element to test.
     * @param input The actual text used in the value attribute to select the option in the dropdown menu.
     * @param fieldName The name of the element to test. This is a made up descriptive name.
     * @throws InterruptedException Needed for sleep.
     */
    public static void setSelectOptionByValue(WebElement element, String input, String fieldName)
            throws InterruptedException {
        Select dropdown = new Select(element);
        dropdown.selectByValue(input);
        logInfo("Value '" + input + "' was chosen from the '" + fieldName + "' dropdown menu.");
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * Set the wait/timeout length for implicit wait and page load to the specified length in seconds.
     * @param driver The Selenium WebDriver object created for the test.
     * @param seconds The amount of time in seconds to wait for an object or page load.
     */
    public static void setTimeoutLength(WebDriver driver, int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
        LOGGER.info("Implicit and page load timeout set to " + seconds + " seconds.");
    }

    /**
     * Verify the page title is as expected.
     * @param driver The Selenium WebDriver object created for the test.
     * @param expectedTitle The expected title of the page.
     */
    public static void verifyPageTitle(WebDriver driver, String expectedTitle) {
        Assert.assertEquals(driver.getTitle(), expectedTitle, "The page title for Echo-Adapt was incorrect: " + driver
                .getTitle());
        logPassStep("The page title is correct: " + driver.getTitle() + ".");
    }

    /**
     * Click on an element and log the event.
     * @param element The actual element to click.
     * @param elementName The name given for the element. This is used for the log message only.
     */
    public static void click(WebElement element, String elementName) {
        element.click();
        logInfo("The " + elementName + " was clicked.");
    }

    /**
     * Same click function, but add a wait time after clicking.
     * @param element The actual element to click.
     * @param elementName The name given for the element. This is used for the log message only.
     * @param waitAfter Number of seconds to wait after clicking the element.
     * @throws InterruptedException Needed for wait.
     */
    public static void click(WebElement element, String elementName, int waitAfter) throws InterruptedException {
        click(element, elementName);
        TimeUnit.SECONDS.sleep(waitAfter);
    }

    /**
     * Click on an element, only log it with LOGGER... not extentreports.
     * @param element The actual element to click.
     * @param elementName The name given for the element. This is used for the log message only.
     */
    public static void clickNoEx(WebElement element, String elementName) {
        element.click();
        LOGGER.info("The " + elementName + " was clicked.");
    }

    /**
     * Same click function, but add a wait time after clicking. Do not log to extentreports.
     * @param element The actual element to click.
     * @param elementName The name given for the element. This is used for the log message only.
     * @param waitAfter Number of seconds to wait after clicking the element.
     * @throws InterruptedException Needed for wait.
     */
    public static void clickNoEx(WebElement element, String elementName, int waitAfter) throws InterruptedException {
        clickNoEx(element, elementName);
        TimeUnit.SECONDS.sleep(waitAfter);
    }

    /**
     * Types the specified text into the specified field and logs the event in logger and extentreports.
     * @param element The actual element to type into.
     * @param elementName The name given for the element. This is used for the log message only.
     * @param input The text to type in the field.
     */
    public static void sendKeys(WebElement element, String elementName, String input) {
        element.click();
        element.clear();
        element.sendKeys(input);
        logInfo("'" + input + "' was typed into the " + elementName + " field.");
    }

    /**
     * Types the specified text into the specified field and logs the event in logger only.
     * @param element The actual element to type into.
     * @param elementName The name given for the element. This is used for the log message only.
     * @param input The text to type in the field.
     */
    public static void sendKeysNoEx(WebElement element, String elementName, String input) {
        element.click();
        element.clear();
        element.sendKeys(input);
        LOGGER.info("'" + input + "' was typed into the " + elementName + " field.");
    }

    public static String getBrowser() {
        return browser;
    }

    public static void setBrowser(String browser) {
        DriverFunctions.browser = browser;
    }

}
