package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainLandingPage extends CorePage {

    private By homePageIdentifier = By.cssSelector("div.header-logo");
    private By socialProofSection = By.cssSelector("section.homepage-social-proof");
    private By differentiatorsSection = By.cssSelector("section.homepage-core-differentiators");
    private By caseStudySection = By.cssSelector("section.homepage-case-study");
    private By channelsSection = By.cssSelector("section.homepage-channels");

    public MainLandingPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageOpened() {
        return this.driver.getCurrentUrl().contains("insiderone.com")
                && this.isElementVisible(this.homePageIdentifier);
    }

    public boolean areMainBlocksLoaded() {
        int visibleCount = 0;

        if (this.isElementVisible(this.socialProofSection)) visibleCount++;
        if (this.isElementVisible(this.differentiatorsSection)) visibleCount++;
        if (this.isElementVisible(this.caseStudySection)) visibleCount++;
        if (this.isElementVisible(this.channelsSection)) visibleCount++;

        return visibleCount >= 3;
    }

}