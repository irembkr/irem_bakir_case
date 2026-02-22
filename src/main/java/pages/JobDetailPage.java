package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class JobDetailPage extends CorePage {

    public JobDetailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnLeverApplicationPage() {
        String currentUrl = getCurrentUrl();
        return currentUrl.startsWith("https://jobs.lever.co/insiderone/");
    }

    public void waitForLeverPageToLoad() {
        // URL "jobs.lever.co"
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(webDriver -> webDriver.getCurrentUrl().startsWith("https://jobs.lever.co/insiderone/"));
    }
}
