package retiredTests;

import automation.DriverManager;
import automation.BrowserType;
import utilities.DvwaEnvironment;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
/*
@Ignore
public class SqlInjectionTest {

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

        // 3. Navigate back to login and wait for page to render
        driver.get(baseUrl + "/login.php");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));

        // 4. Log in again after the forced logout
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("Login")).click();

        // 5. Drop the security level to "Low"
        driver.get(baseUrl + "/security.php");
        Select securityDropdown = new Select(driver.findElement(By.name("security")));
        securityDropdown.selectByValue("low");
        driver.findElement(By.name("seclev_submit")).click();
    }

    // Priority 1: Proves the exploit works by checking for unauthorized data
    @Test(priority = 1)
    public void testSqliProofOfConcept() throws InterruptedException {
        var driver = DriverManager.getDriver();
        driver.get(DvwaEnvironment.getBaseUrl() + "/vulnerabilities/sqli/");

        // Inject the SQLi payload
        String sqliPayload = "' OR '1'='1";
        driver.findElement(By.name("id")).sendKeys(sqliPayload);
        driver.findElement(By.name("Submit")).click();

        // Pause so the audience can see the massive database dump on screen
        Thread.sleep(3000);

        // Verify the exploit: Gordon Brown is User ID 2.
        // If his name appears when we didn't search for ID 2, the DB was dumped.
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.contains("Surname: Brown"),
                "SQL Injection failed. The database did not dump all users.");

        System.out.println("PoC Test Passed: SQL Injection vulnerability confirmed.");
    }

    // Priority 2: Acts as a security QA gate (FAILS when SQLi succeeds)
    @Test(priority = 2)
    public void testSqliDefensiveRegression() throws InterruptedException {
        var driver = DriverManager.getDriver();
        driver.get(DvwaEnvironment.getBaseUrl() + "/vulnerabilities/sqli/");

        driver.findElement(By.name("id")).sendKeys("' OR '1'='1");
        driver.findElement(By.name("Submit")).click();

        Thread.sleep(3000);

        String pageSource = driver.getPageSource();

        // If the payload successfully retrieves other users, fail the build
        if (pageSource.contains("Surname: Brown")) {
            Assert.fail("VULNERABILITY DETECTED: The SQL Injection payload successfully dumped the database.");
        } else {
            System.out.println("Regression Test Passed: The application sanitized the SQL query.");
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
}*/