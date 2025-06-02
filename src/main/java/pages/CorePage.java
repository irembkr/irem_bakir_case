package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CorePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    private By insiderLogo = By.cssSelector("img[alt='insider_logo']");

    public CorePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // Waits for element visibility also waits for full page load
    protected void waitForVisibility(By locator) {
        WebDriverWait pageLoadWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        pageLoadWait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Waits until the element is clickable
    protected void waitForClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Performs click action
    protected void click(By locator) {
        waitForClickable(locator);
        driver.findElement(locator).click();
    }

    // Types text into the element
    protected void type(By locator, String text) {
        waitForVisibility(locator);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    // Checks if element is visible
    protected boolean isElementVisible(By locator) {
        try {
            waitForVisibility(locator);
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Returns current URL
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // Checks if homepage is loaded
    public boolean isHomePageOpened() {
        return isElementVisible(insiderLogo);
    }

    // 🔄 Newly added: Finds multiple elements
    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
}
