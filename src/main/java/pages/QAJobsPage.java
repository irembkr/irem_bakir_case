package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class QAJobsPage {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public QAJobsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        this.js = (JavascriptExecutor) driver;
    }

    public void goToQAJobsPage() {
        driver.get("https://useinsider.com/careers/quality-assurance/");

        try {
            WebElement acceptCookiesButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("wt-cli-accept-all-btn")));
            acceptCookiesButton.click();
        } catch (Exception e) {

        }
    }

    public void clickSeeAllJobsButton() {
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));

        for (int i = 0; i < 10; i++) {
            try {
                WebElement seeAllJobsButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(text(),'See all QA jobs')]")));

                js.executeScript("arguments[0].scrollIntoView(true);", seeAllJobsButton);
                js.executeScript("arguments[0].click();", seeAllJobsButton);

                wait.until(driver -> ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState").equals("complete"));
                Thread.sleep(2000);

                return;
            } catch (Exception e) {
                js.executeScript("window.scrollBy(0, 300)");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }

        throw new RuntimeException("Could not click the 'See all QA jobs' button.");
    }

    public void filterByLocationAndDepartment(String location, String department) {
        try {
            System.out.println("Starting filtering process for Location and Department...");

            Thread.sleep(2000);

            WebElement locationFilter = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@id='select2-filter-by-location-container']/ancestor::span[@role='combobox']")));
            locationFilter.click();

            WebElement locationOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(@class,'select2-results__option') and normalize-space(text())='Istanbul, Turkiye']")));
            locationOption.click();

            Thread.sleep(2000);

            WebElement departmentFilter = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("span#select2-filter-by-department-container")));
            departmentFilter.click();

            WebElement departmentOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(text(),'" + department + "')]")));
            departmentOption.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".position-list-item-loading")));

            System.out.println("Filters applied: Location = " + location + " | Department = " + department);
        } catch (InterruptedException e) {
            throw new RuntimeException("Filtering wait was interrupted", e);
        }
    }

    public List<WebElement> getOpenPositions() {
        return driver.findElements(By.cssSelector("div.position-list-item"));
    }

    // ✅ UPDATED METHOD
    public void clickFirstViewRole() {
        WebElement firstJobCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.position-list-item")));

        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", firstJobCard);

        Actions actions = new Actions(driver);
        actions.moveToElement(firstJobCard).perform();

        WebElement viewRoleButton = wait.until(ExpectedConditions.elementToBeClickable(
                firstJobCard.findElement(By.cssSelector("a.btn.btn-navy"))));

        js.executeScript("arguments[0].click();", viewRoleButton);
    }

    public boolean areAllResultsValid(String location, String department) {
        List<WebElement> positions = getOpenPositions();
        for (WebElement position : positions) {
            String locText = "";
            String deptText = "";

            try {
                locText = position.findElement(By.cssSelector("div.position-location")).getText().toLowerCase();
            } catch (Exception e) {
                System.out.println("Location information not found!");
                return false;
            }

            try {
                deptText = position.findElement(By.cssSelector("span.position-department")).getText().toLowerCase();
            } catch (Exception e) {
                System.out.println("Department information not found!");
                return false;
            }

            if (!locText.contains(location.toLowerCase()) || !deptText.contains(department.toLowerCase())) {
                System.out.println("MISMATCH FOUND:");
                System.out.println("Location: " + locText);
                System.out.println("Department: " + deptText);
                return false;
            }
        }
        return true;
    }
}
