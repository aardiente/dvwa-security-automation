import org.automation.BrowserType;
import org.automation.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.Locale;

public class SetupTest
{
    @BeforeMethod
    @Parameters("browser")
    public void setup(@Optional("Chrome") String brow)
    {
        BrowserType type = BrowserType.valueOf(brow.toUpperCase());

        DriverManager.initDriver(type);

    }

    @Test
    public void testSuccesfulLogin()
    {
        var driver = DriverManager.getDriver();

        // 1. Navigate to the application
        driver.get("https://www.saucedemo.com/");

        // 2. Enter credentials and click login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // 3. Verify login success by checking if the URL changed to the inventory page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean isLoggedIn = wait.until(ExpectedConditions.urlContains("inventory.html"));

        Assert.assertTrue(isLoggedIn, "The login failed or the dashboard did not load.");
    }
    @AfterMethod
    public void teardown() {
        // Cleans up the browser instance
        DriverManager.quitDriver();
    }
}
