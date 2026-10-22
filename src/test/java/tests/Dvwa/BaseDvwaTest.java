
package tests.Dvwa;

import automation.ZapManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pom.Dvwa.*;
import utilities.Docker.DvwaEnvironment;
import utilities.ExcelFileReader;

public abstract class BaseDvwaTest {

    protected static DvwaEnvironment container;
    protected static String baseUrl;
    protected String payloadSource = "";
    protected String[] securityLevels = {"low", "medium", "high"};

    @BeforeSuite
    public void startServer() {
        if (container != null) return;

        container = new DvwaEnvironment();
        container.startEnvironment();
        baseUrl = container.getInternalURL();

        ZapManager.initZap();
    }

    @AfterSuite(alwaysRun = true)
    public void stopServer() {
        if (container == null) return;

        try {
            ZapManager.generateReport("zap-dvwa.html");
        } finally {
            try {
                ZapManager.quitZap();
            } finally {
                container.stopEnvironment();
                container = null;
            }
        }
    }

    @BeforeMethod
    public void initBrowser() {
        ZapManager.initZapDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        ZapManager.quitZapDriver();
    }

    @BeforeClass
    public void initDvwaDatabase() {
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
    public Object[][] getSecurityAndPayloads() {
        Object[][] payloadsFromExcel = ExcelFileReader.extractData(payloadSource, null);
        Object[][] testData = new Object[payloadsFromExcel.length * securityLevels.length][2];

        int index = 0;

        for (String level : securityLevels) {
            for (Object[] row : payloadsFromExcel) {
                testData[index][0] = level;
                testData[index][1] = row[0].toString();
                index++;
            }
        }

        return testData;
    }
}
