import org.automation.DriverManager;
import org.automation.BrowserType;
import org.utilities.DvwaEnvironment;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class ReflectedXssTest {

    @BeforeSuite
    public void startServer() {
        DvwaEnvironment.startEnvironment();
    }

    @BeforeMethod
    public void setupAndLogin() {
        DriverManager.initDriver(BrowserType.CHROME);
        var driver = DriverManager.getDriver();
        String baseUrl = DvwaEnvironment.getBaseUrl();

        // 1. Initial Login
        driver.get(baseUrl + "/login.php");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("Login")).click();

        // 2. Initialize the Database
        driver.get(baseUrl + "/setup.php");
        driver.findElement(By.name("create_db")).click();

        // --- THE FIX ---
        // Manually navigate back to login since DVWA stays on setup.php
        driver.get(baseUrl + "/login.php");

        // Wait for the username field to be visible so the slow-mo listener doesn't misfire
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        // ---------------

        // 3. Log in again after the forced logout
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("Login")).click();

        // 4. Drop the security level to "Low"
        driver.get(baseUrl + "/security.php");
        Select securityDropdown = new Select(driver.findElement(By.name("security")));
        securityDropdown.selectByValue("low");
        driver.findElement(By.name("seclev_submit")).click();
    }

    // Priority 1: Proves the exploit works (PASSES when XSS executes)
    @Test(priority = 1)
    public void testExploitProofOfConcept() throws InterruptedException {
        var driver = DriverManager.getDriver();
        driver.get(DvwaEnvironment.getBaseUrl() + "/vulnerabilities/xss_r/");

        driver.findElement(By.name("name")).sendKeys("<script>alert('Proof_of_Concept_Success')</script>");
        driver.findElement(By.cssSelector("input[value='Submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.alertIsPresent());

        // Pause to see the alert
        Thread.sleep(3000);

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();

        Assert.assertEquals(alertText, "Proof_of_Concept_Success", "XSS payload failed to execute.");
        System.out.println("PoC Test Passed: Vulnerability confirmed.");
    }

    // Priority 2: Acts as a security gate (FAILS when XSS executes)
    @Test(priority = 2)
    public void testDefensiveSecurityRegression() throws InterruptedException {
        var driver = DriverManager.getDriver();
        driver.get(DvwaEnvironment.getBaseUrl() + "/vulnerabilities/xss_r/");

        driver.findElement(By.name("name")).sendKeys("<script>alert('Security_Regression_Failure')</script>");
        driver.findElement(By.cssSelector("input[value='Submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        try {
            wait.until(ExpectedConditions.alertIsPresent());

            // Pause to see the alert before it fails the test
            Thread.sleep(3000);

            driver.switchTo().alert().accept();

            Assert.fail("VULNERABILITY DETECTED: The XSS payload successfully executed.");

        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Regression Test Passed: The application sanitized the XSS payload.");
        }
    }

    @AfterMethod
    public void teardown() {
        DriverManager.quitDriver();
    }

    @AfterSuite
    public void stopServer() {
        DvwaEnvironment.stopEnvironment();
    }
}