package retiredTests;
/*
import org.openqa.selenium.By;
import utilities.DvwaEnvironment;

import automation.BrowserType;
import automation.DriverManager;
import org.testng.annotations.*;

@Ignore
public class DvwaSecurityTest {

    @BeforeSuite
    public void startServer() {
        DvwaEnvironment.startEnvironment();
    }

    @BeforeMethod
    public void setup() {
        DriverManager.initDriver(BrowserType.CHROME);
    }

    @Test
    public void initializeDvwaDatabase() throws InterruptedException {
        var driver = DriverManager.getDriver();
        String baseUrl = DvwaEnvironment.getBaseUrl();

        // 1. Navigate to the Login Page
        driver.get(baseUrl + "/login.php");
        System.out.println("Navigated to: " + driver.getCurrentUrl());

        // 2. Log in with default DVWA credentials
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("Login")).click();

        // 3. Navigate to the setup page
        driver.get(baseUrl + "/setup.php");

        // 4. Click the "Create / Reset Database" button
        driver.findElement(By.name("create_db")).click();
        System.out.println("Database initialization triggered.");

        // Pause for 5 seconds so you can physically see the browser before it closes
        // (Remove this Thread.sleep once you start writing real automation tests)
        Thread.sleep(5000);
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