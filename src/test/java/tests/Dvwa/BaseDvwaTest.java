package tests.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.testng.annotations.*;

import automation.ZapManager;

import utilities.Docker.DvwaEnvironment;

import pom.Dvwa.*;
import utilities.ExcelFileReader;

public abstract class BaseDvwaTest
{
    protected static DvwaEnvironment container;
    protected static String baseUrl;
    protected String payloadSource = "";
    protected String[] securityLevels = {"low", "medium", "high"};

    /********************************************************************/

    @BeforeSuite
    public void startServer()
    {
        container = new DvwaEnvironment();
        container.startEnvironment();
        baseUrl = container.getInternalURL();

        ZapManager.initZap();
    }

    @AfterSuite(alwaysRun = true)
    public void stopServer()
    {
        ZapManager.generateReport();
        ZapManager.quitZap();

        if(container != null)
            container.stopEnvironment();
    }
    /********************************************************************/

    @BeforeMethod
    public void initBrowser() { ZapManager.initZapDriver(); }

    @AfterMethod
    public void closeBrowser() { ZapManager.quitZapDriver(); }

    /********************************************************************/
    @BeforeClass
    public void initDvwaDatabase()
    {
        ZapManager.initZapDriver();
        WebDriver driver = ZapManager.getDriver();
        LoginPage loginPage = new LoginPage(driver);

        driver.get(baseUrl + loginPage.loginEndpoint);
        loginPage.login("admin", "password");

        driver.get(baseUrl + "/setup.php");
        driver.findElement(By.name("create_db")).click();
        System.out.println("Global DB Initialized");

        ZapManager.quitZapDriver();
    }

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
}