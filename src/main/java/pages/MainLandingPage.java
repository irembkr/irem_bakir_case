package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainLandingPage extends CorePage {

    private By companyMenu = By.xpath("//a[@id='navbarDropdownMenuLink' and contains(text(),'Company')]");
    private By careersSubMenu = By.xpath("//a[contains(@href, '/careers/') and text()='Careers']");

    public MainLandingPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageOpened() {
        return isElementVisible(companyMenu);
    }

    public void goToCareersPage() {
        click(companyMenu);  // Open the dropdown
        waitForVisibility(careersSubMenu);  // Wait until menu is visible
        click(careersSubMenu); // Click on the Careers link
    }
}
