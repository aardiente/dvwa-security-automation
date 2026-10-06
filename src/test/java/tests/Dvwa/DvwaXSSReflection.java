package tests.Dvwa;

import automation.DriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pom.Dvwa.LoginPage;
import pom.Dvwa.SecurityPage;

import java.time.Duration;

public class DvwaXSSReflection extends BaseDvwaTest
{
    @DataProvider(name = "securityLevels")
    public Object[][] getSecurityLevels()
    {
        return new Object[][]{ {"low"}, {"medium"}, {"high"} };
    }


    /*******************************************************************************/
    // Tests
    @Test(dataProvider = "securityLevels")
    public void XSS_Reflected(String levelStr) throws InterruptedException
    {
        var driver = DriverManager.getDriver();
        String baseUrl = container.getURL();

        pom.Dvwa.SecurityPage.SecurityLevel securityLevel = null;
        for (pom.Dvwa.SecurityPage.SecurityLevel lvl : pom.Dvwa.SecurityPage.SecurityLevel.values())
        {
            if (lvl.getSecurityLevel().equalsIgnoreCase(levelStr))
            {
                securityLevel = lvl;
                break;
            }
        }

        // 1. Authenticate
        driver.get(baseUrl + LoginPage.loginEndpoint);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin", "password");

        // 2. Adjust Security Level
        driver.get(baseUrl + SecurityPage.securityEndpoint);
        SecurityPage securityPage = new SecurityPage(driver);
        securityPage.setSecurityLevel(securityLevel);

        // 3. Navigate to the targeted view using the page model sidebar actions
        loginPage.navigateToXssReflected();

        // 4. Inject payload
        driver.findElement(By.name("name")).sendKeys("<script>alert('Proof_of_Concept_Success')</script>");
        driver.findElement(By.cssSelector("input[value='Submit']")).click();

        boolean xssExecuted = false;

        try
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.alertIsPresent());

            Alert alert = driver.switchTo().alert();
            alert.accept();

            xssExecuted = true;
        } catch (org.openqa.selenium.TimeoutException e)
        {
            System.out.println(e.getLocalizedMessage());
        }

        Assert.assertFalse(xssExecuted, "Xss Vulnerability found in XSS Reflected Page");
    }
}
