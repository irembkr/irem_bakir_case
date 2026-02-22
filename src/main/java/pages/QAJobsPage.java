package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class QAJobsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    public QAJobsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        this.js = (JavascriptExecutor) driver;
    }

    public void goToQAJobsPage() {
        driver.get("https://insiderone.com/careers/quality-assurance/");
        try {
            WebElement acceptCookiesButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("wt-cli-accept-all-btn"))
            );
            acceptCookiesButton.click();
        } catch (Exception e) {
        }
    }

    public void clickSeeAllJobsButton() {
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));

        for (int i = 0; i < 10; i++) {
            try {
                WebElement seeAllJobsButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(text(),'See all QA jobs')]")
                ));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", seeAllJobsButton);
                js.executeScript("arguments[0].click();", seeAllJobsButton);
                wait.until(d -> ((JavascriptExecutor) d)
                        .executeScript("return document.readyState").equals("complete"));
                Thread.sleep(6000);
                return;
            } catch (Exception e) {
                js.executeScript("window.scrollBy(0, 300)");
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }

        throw new RuntimeException("couldn't click 'See all QA jobs' after 10 attempts");
    }

    public void filterByLocationAndDepartment(String location, String department) {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(25));

        try {
            // location dropdown
            By locationSelectBy = By.cssSelector("select#filter-by-location, select[name='filter-by-location']");
            WebElement locationSelectEl = longWait.until(
                    ExpectedConditions.visibilityOfElementLocated(locationSelectBy)
            );
            longWait.until(d -> locationSelectEl.findElements(By.tagName("option")).size() > 1);
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", locationSelectEl);
            Thread.sleep(1500);
            new Select(locationSelectEl).selectByVisibleText(location);

            // department dropdown
            By deptSelectBy = By.cssSelector("select#filter-by-department, select[name='filter-by-department']");
            WebElement deptSelectEl = longWait.until(
                    ExpectedConditions.visibilityOfElementLocated(deptSelectBy)
            );
            longWait.until(d -> deptSelectEl.findElements(By.tagName("option")).size() > 1);
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", deptSelectEl);
            Thread.sleep(1200);
            new Select(deptSelectEl).selectByVisibleText(department);

            // wait for loader to disappear if there is one
            try {
                longWait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector(".position-list-item-loading")
                ));
            } catch (Exception e) {}

            longWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.cssSelector("div.position-list-item"), 0
            ));
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            throw new RuntimeException("filtering interrupted", e);
        }
    }

    private List<WebElement> getOpenPositions() {
        return driver.findElements(By.cssSelector("div.position-list-item"));
    }

    public void clickFirstViewRole() {
        WebElement firstJobCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.position-list-item")
        ));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstJobCard);
        new Actions(driver).moveToElement(firstJobCard).perform();

        WebElement viewRoleButton = wait.until(ExpectedConditions.elementToBeClickable(
                firstJobCard.findElement(By.cssSelector("a.btn.btn-navy"))
        ));
        js.executeScript("arguments[0].click();", viewRoleButton);
    }

    public boolean areAllResultsValid(String location, String department) {
        List<WebElement> positions = getOpenPositions();

        if (positions.isEmpty()) {
            System.out.println("no positions found after filtering");
            return false;
        }

        for (WebElement position : positions) {
            String locText;
            String deptText;
            String titleText;

            try {
                locText = position.findElement(By.cssSelector("div.position-location"))
                        .getText().trim().toLowerCase();
                deptText = position.findElement(By.cssSelector("span.position-department"))
                        .getText().trim().toLowerCase();
                titleText = position.findElement(By.cssSelector("p.position-title"))
                        .getText().trim().toLowerCase();
            } catch (Exception e) {
                System.out.println("couldn't read job card details");
                return false;
            }

            boolean locationMatch = locText.contains(location.toLowerCase());
            boolean departmentMatch = deptText.contains(department.toLowerCase());
            boolean titleMatch = titleText.contains("quality") || titleText.contains("qa");

            if (!locationMatch || !departmentMatch || !titleMatch) {
                System.out.println("mismatch on card:");
                System.out.println("  title: " + titleText);
                System.out.println("  location: " + locText);
                System.out.println("  department: " + deptText);
                return false;
            }
        }

        return true;
    }
}