package tests.JuiceShop;

import automation.ZapManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utilities.Docker.JuiceShopEnvironment;


public abstract class JuiceShopBaseTest
{
    protected static JuiceShopEnvironment container;
    protected static String baseUrl;
    protected String payloadSource = "";

    @BeforeSuite
    public void startServer()
    {
        container = new JuiceShopEnvironment();
        container.startServer();
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

    @BeforeMethod
    public void initBrowser() { ZapManager.initZapDriver(); }

    @AfterMethod
    public void closeBrowser() { ZapManager.quitZapDriver(); }
}