package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.CareersPage;
import pages.JobDetailPage;
import pages.MainLandingPage;
import pages.QAJobsPage;

import java.time.Duration;

public class InsiderTest {

    private WebDriver driver;
    private MainLandingPage mainLandingPage;
    private CareersPage careersPage;
    private QAJobsPage qaJobsPage;
    private JobDetailPage jobDetailPage;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private static boolean allTestsPassed = true;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        mainLandingPage = new MainLandingPage(driver);
        careersPage = new CareersPage(driver);
        qaJobsPage = new QAJobsPage(driver);
        jobDetailPage = new JobDetailPage(driver);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;

        System.out.println("Step 1: Visiting https://useinsider.com/ and verifying that the home page is opened...");
        driver.get("https://useinsider.com/");
        Assert.assertTrue(mainLandingPage.isPageOpened(), "Insider homepage did not open successfully.");

        System.out.println("Step 2: Navigating to the Careers page from the Company menu...");
        mainLandingPage.goToCareersPage();

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("wt-cli-accept-all-btn"))).click();
            Thread.sleep(1000);
        } catch (Exception e) {

        }
    }

    @Test
    public void testCareerPageSectionsVisible() throws InterruptedException {
        System.out.println("Step 3: Verifying that the 'Teams' section is visible on the Careers page...");
        scrollBySteps(js, 17, 100, 500);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[normalize-space()='Find your calling']")));
        Assert.assertTrue(careersPage.isTeamsSectionVisible(), "'Teams' section is not visible.");

        System.out.println("Step 4: Verifying that the 'Locations' section is visible on the Careers page...");
        scrollBySteps(js, 7, 100, 500);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@class='category-title-media ml-0']")));
        Assert.assertTrue(careersPage.isLocationsSectionVisible(), "'Locations' section is not visible.");

        System.out.println("Step 5: Verifying that the 'Life at Insider' section is visible on the Careers page...");
        scrollBySteps(js, 5, 100, 500);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Life at Insider']")));
        Assert.assertTrue(careersPage.isLifeAtInsiderSectionVisible(), "'Life at Insider' section is not visible.");
    }

    @Test
    public void testQAJobsFlow() throws InterruptedException {
        System.out.println("Step 6: Scrolling down to the QA section...");
        scrollBySteps(js, 30, 100, 300);

        System.out.println("Step 7: Navigating to https://useinsider.com/careers/quality-assurance/...");
        qaJobsPage.goToQAJobsPage();

        System.out.println("Step 8: Clicking the 'See all QA jobs' button...");
        qaJobsPage.clickSeeAllJobsButton();

        System.out.println("Step 9: Filtering jobs by Location: 'Istanbul, Turkey' and Department: 'Quality Assurance'...");
        Thread.sleep(2000);
        qaJobsPage.filterByLocationAndDepartment("Istanbul, Turkiye", "Quality Assurance");
        Thread.sleep(2000);
        System.out.println("Filters applied: Location = Istanbul, Turkey | Department = Quality Assurance");

        js.executeScript("window.scrollBy(0, 600)");
        Thread.sleep(6000);

        System.out.println("Step 10: Verifying that all job listings match the selected filters...");
        Assert.assertTrue(qaJobsPage.areAllResultsValid("Istanbul, Turkiye", "Quality Assurance"),
                "Some job listings do not match the selected location or department filters.");

        System.out.println("Step 11: Clicking the 'View Role' button on the first QA job listing...");
        qaJobsPage.clickFirstViewRole();

        System.out.println("Step 12: Switching to the job detail tab...");
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }

        System.out.println("Step 13: Waiting for the Lever application page to load...");
        jobDetailPage.waitForLeverPageToLoad();

        System.out.println("Step 14: Verifying that the user is redirected to the Lever application form page...");
        Assert.assertTrue(jobDetailPage.isOnLeverApplicationPage(), "Redirection to Lever application page failed.");
    }

    private void scrollBySteps(JavascriptExecutor js, int times, int pixels, int waitMillis) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            js.executeScript("window.scrollBy(0, " + pixels + ")");
            Thread.sleep(waitMillis);
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            driver.quit();
        }

        if (result.getStatus() == ITestResult.SUCCESS) {
            // System.out.println("✅ Test completed successfully: " + result.getName());
        } else if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("❌ Test failed: " + result.getName());
            allTestsPassed = false;
        } else if (result.getStatus() == ITestResult.SKIP) {
            System.out.println("⚠️ Test skipped: " + result.getName());
            allTestsPassed = false;
        }
    }

    @AfterSuite
    public void afterAllTests() {
        if (allTestsPassed) {
            System.out.println("✅✅✅ All tests completed successfully.");
        }
    }
}
