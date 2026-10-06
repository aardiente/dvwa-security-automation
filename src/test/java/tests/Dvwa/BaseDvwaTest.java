package tests.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.testng.annotations.*;

import automation.BrowserType;
import automation.DriverManager;

import utilities.DvwaEnvironment;

import pom.Dvwa.*;

public abstract class BaseDvwaTest
{
    protected static DvwaEnvironment container;
    protected static String baseUrl;

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




}
