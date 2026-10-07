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
import pom.Dvwa.XssReflectedPage;
import utilities.ExcelFileReader;

import java.time.Duration;

public class DvwaXSSReflection extends BaseDvwaTest
{
    @DataProvider(name = "securityAndPayloads")
    public Object[][] getSecurityAndPayloads()
    {
        Object[][] payloadsFromExcel = ExcelFileReader.extractData("src/test/java/data/xss_payloads.xlsx", null);

        String[] securityLevels = {"low", "medium", "high"};

        Object[][] testData = new Object[payloadsFromExcel.length * securityLevels.length][2];

        int index = 0;
        for (String level : securityLevels) {
            for (Object[] row : payloadsFromExcel) {
                testData[index][0] = level;

                // Extracting the payload from the first column of the Excel row
                testData[index][1] = row[0].toString();
                index++;
            }
        }

        return testData;
    }

    @Test(dataProvider = "securityAndPayloads")
    public void XSS_Reflected(String levelStr, String payload) throws InterruptedException
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

        driver.get(baseUrl + LoginPage.loginEndpoint);
        LoginPage loginPage = new LoginPage(driver);

        //loginPage.waitForPageLoad(LoginPage.loginEndpoint);
        loginPage.login("admin", "password");

        driver.get(baseUrl + SecurityPage.securityEndpoint);
        SecurityPage securityPage = new SecurityPage(driver);

        //securityPage.waitForPageLoad(SecurityPage.securityEndpoint);
        securityPage.setSecurityLevel(securityLevel);

        loginPage.navigateToXssReflected();

        //securityPage.waitForPageLoad(SecurityPage.securityEndpoint);

        WebDriverWait pageSettlementWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        pageSettlementWait.until(ExpectedConditions.urlContains("xss_r"));

        securityPage.enterPayload(payload);
        securityPage.clickSubmitPayload();

        boolean xssExecuted = false;

        try
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.alertIsPresent());

            Alert alert = driver.switchTo().alert();
            System.out.println("Visual Verification Confirmed: " + alert.getText());

            // Keep this sleep context active so you can visually verify the pop up
            //Thread.sleep(3000);

            alert.accept();
            xssExecuted = true;
        }
        catch (org.openqa.selenium.TimeoutException e)
        {
            System.out.println("Payload execution blocked: " + e.getLocalizedMessage());
        }

        Assert.assertFalse(xssExecuted, "Targeted XSS Exploit succesfully fired under setting [" + levelStr.toUpperCase() + "]");
    }


}
