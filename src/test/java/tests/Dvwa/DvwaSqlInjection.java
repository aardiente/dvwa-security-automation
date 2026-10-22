package tests.Dvwa;

import automation.ZapManager;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pom.Dvwa.LoginPage;
import pom.Dvwa.SqlInjectionPage;
import utilities.ExcelFileReader;

import java.time.Duration;

public class DvwaSqlInjection extends BaseDvwaTest
{
    private final String payloadSource = "src/test/java/data/sqli_payloads.xlsx";
    private final String[] securityLevels = {"low"};

    @DataProvider(name = "securityAndPayloads")
    public Object[][] getSecurityAndPayloads()
    {
        Object[][] payloadsFromExcel = ExcelFileReader.extractData(payloadSource, null);

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
    public void sqlInjection_Low(String s, String payload)
    {
        var driver = ZapManager.getDriver();

        driver.get(baseUrl + LoginPage.loginEndpoint);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin", "password");
        loginPage.navigateToSqlInjection();

        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        pageWait.until(ExpectedConditions.urlContains("sqli"));

        SqlInjectionPage sqlI = new SqlInjectionPage(driver);
        sqlI.injectPayload(payload);

        String pageSource = driver.getPageSource();
        Assert.assertFalse(pageSource.contains("Surname: Brown"), "SqlI Executed: " + payload);
    }
}