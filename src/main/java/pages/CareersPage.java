package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CareersPage {
    private WebDriver driver;

    private By teamsSection = By.xpath("//h3[normalize-space()='Find your calling']");
    private By locationsSection = By.xpath("//h3[@class='category-title-media ml-0']");
    private By lifeAtInsiderSection = By.xpath("//h2[normalize-space()='Life at Insider']");

    public CareersPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isTeamsSectionVisible() {
        return driver.findElement(teamsSection).isDisplayed();
    }

    public boolean isLocationsSectionVisible() {
        return driver.findElement(locationsSection).isDisplayed();
    }

    public boolean isLifeAtInsiderSectionVisible() {
        return driver.findElement(lifeAtInsiderSection).isDisplayed();
    }
}
