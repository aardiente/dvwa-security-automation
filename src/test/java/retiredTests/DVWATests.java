package retiredTests;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import utilities.DvwaEnvironment;

import automation.BrowserType;
import automation.DriverManager;
import org.testng.annotations.*;

import pom.Dvwa.*;

public class DVWATests
{
    /*******************************************************************************/
    // Resources
    private DvwaEnvironment container;

    /*******************************************************************************/
    // Docker Server Handlers
    @BeforeSuite
    private void startServer(){ this.container = new DvwaEnvironment(); this.container.startEnvironment(); }
    @AfterSuite
    private void stopServer(){ this.container.stopEnvironment(); }

    /*******************************************************************************/
    // Selenium Browser Handlers
    @BeforeMethod
    private void initBrowser(){ DriverManager.initDriver(BrowserType.CHROME); }
    @AfterMethod
    private void closeBrowser(){ DriverManager.quitDriver(); }

    /*******************************************************************************/
    // Container Helpers
    @BeforeClass
    private void initDvwaDatabase()
    {
        LoginPage loginPage = new LoginPage( DriverManager.getDriver() );
        String baseUrl = container.getURL();

        var driver = loginPage.getDriver();
        driver.get(baseUrl + loginPage.loginEndpoint);
        System.out.println("Navigated to: " + driver.getCurrentUrl());

        loginPage.login("admin", "password");

        driver.get(baseUrl + "/setup.php");

        driver.findElement(By.name("create_db")).click();
        System.out.println("Database initialized");

    }

    /*******************************************************************************/
    // Tests
    @Test
    public void XSS_Reflected() throws InterruptedException
    {
        var driver = DriverManager.getDriver();
        String baseUrl = this.container.getURL();

        // 1. Authenticate
        driver.get(baseUrl + "/login.php");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin", "password");

        // 2. Navigate to the targeted view using the page model sidebar actions
        loginPage.navigateToXssReflected();

        // 3. Inject payload
        driver.findElement(By.name("name")).sendKeys("<script>alert('Proof_of_Concept_Success')</script>");
        driver.findElement(By.cssSelector("input[value='Submit']")).click();

        // 4. Validate alert behavior
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.alertIsPresent());

        Thread.sleep(3000);

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();

        Assert.assertEquals(alertText, "Proof_of_Concept_Success", "XSS payload failed to execute.");
        System.out.println("PoC Test Passed: Vulnerability confirmed.");
    }
}
