package tests.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.testng.annotations.*;

import automation.BrowserType;
import automation.DriverManager;

import utilities.DvwaEnvironment;

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
        baseUrl = container.getURL();
    }

    @AfterSuite(alwaysRun = true)
    public void stopServer()
    {
        if(container != null)
            container.stopEnvironment();
    }
    /********************************************************************/

    @BeforeMethod
    public void initBrowser() { DriverManager.initDriver(BrowserType.CHROME); }
    @AfterMethod
    public void closeBrowser(){ DriverManager.quitDriver(); }

    /********************************************************************/
    @BeforeClass
    public void initDvwaDatabase()
    {
        DriverManager.initDriver(BrowserType.CHROME);
        WebDriver driver = DriverManager.getDriver();
        LoginPage loginPage = new LoginPage(driver);

        driver.get(baseUrl + loginPage.loginEndpoint);
        loginPage.login("admin", "password");

        driver.get(baseUrl + "/setup.php");
        driver.findElement(By.name("create_db")).click();
        System.out.println("Global DB Initialized");

        DriverManager.quitDriver();
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
