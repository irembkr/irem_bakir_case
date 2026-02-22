package tests;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.JobDetailPage;
import pages.MainLandingPage;
import pages.QAJobsPage;

import java.time.Duration;

public class InsiderTest {

    private WebDriver driver;
    private MainLandingPage mainLandingPage;
    private QAJobsPage qaJobsPage;
    private JobDetailPage jobDetailPage;
    private JavascriptExecutor js;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();

        mainLandingPage = new MainLandingPage(driver);
        qaJobsPage = new QAJobsPage(driver);
        jobDetailPage = new JobDetailPage(driver);
        js = (JavascriptExecutor) driver;

        // step 1 : open homepage
        driver.get("https://insiderone.com/");
        waitForDocumentReady(60);
        presetCookieConsentAndRefresh();
        waitForDocumentReady(60);

        Assert.assertTrue(mainLandingPage.isPageOpened(), "Homepage did not load.");
        Assert.assertTrue(mainLandingPage.areMainBlocksLoaded(), "Main sections are missing.");
        System.out.println("Step 1 done - homepage and main sections loaded");
    }

    @Test
    public void testQAJobsFlow() {
        // step 2 : navigate to QA careers page
        qaJobsPage.goToQAJobsPage();
        waitForDocumentReady(60);
        presetCookieConsentAndRefresh();
        waitForDocumentReady(60);

        qaJobsPage.clickSeeAllJobsButton();
        System.out.println("Step 2 done - clicked See all QA jobs");

        // step 3 : apply filters and verify job cards
        qaJobsPage.filterByLocationAndDepartment("Istanbul, Turkiye", "Quality Assurance");
        js.executeScript("window.scrollBy(0, 200)");

        Assert.assertTrue(
                qaJobsPage.areAllResultsValid("Istanbul, Turkiye", "Quality Assurance"),
                "Job listings don't match the applied filters."
        );
        System.out.println("Step 3 done - all job cards match title/department/location");

        // step 4 : click View Role verify Lever redirect
        qaJobsPage.clickFirstViewRole();

        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }

        jobDetailPage.waitForLeverPageToLoad();
        Assert.assertTrue(jobDetailPage.isOnLeverApplicationPage(), "Lever page did not open.");
        System.out.println("Step 4 done - Lever application page opened");
    }

    private void presetCookieConsentAndRefresh() {
        try {
            // set consent cookies so the banner doesn't block the test
            driver.manage().addCookie(new Cookie.Builder("viewed_cookie_policy", "yes").path("/").build());
            driver.manage().addCookie(new Cookie.Builder("CookieLawInfoConsent", "yes").path("/").build());
            driver.manage().addCookie(new Cookie.Builder("cookielawinfo-checkbox-necessary", "yes").path("/").build());
            driver.manage().addCookie(new Cookie.Builder("cookielawinfo-checkbox-functional", "yes").path("/").build());
            driver.manage().addCookie(new Cookie.Builder("cookielawinfo-checkbox-performance", "yes").path("/").build());
            driver.manage().addCookie(new Cookie.Builder("cookielawinfo-checkbox-analytics", "yes").path("/").build());
            driver.manage().addCookie(new Cookie.Builder("cookielawinfo-checkbox-advertisement", "yes").path("/").build());
            driver.manage().addCookie(new Cookie.Builder("cookielawinfo-checkbox-others", "yes").path("/").build());
            driver.navigate().refresh();
        } catch (Exception e) {

        }
    }

    // waits until page is fully loaded (for stable runs on Maven)
    private void waitForDocumentReady(int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}