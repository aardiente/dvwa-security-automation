package automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.time.Duration;

public class DriverManager
{
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final int DefaultTimeOut = 10;

    private DriverManager(){}

    public static void initDriver(BrowserType brow)
    {
        if(driver.get() == null)
        {
            WebDriver webDriver = BrowserFactory.createDriver(brow);
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DefaultTimeOut));

            SlowMoListener listener = new SlowMoListener(webDriver);
            WebDriver decoratedDriver = new EventFiringDecorator<>(listener).decorate(webDriver);

            driver.set(decoratedDriver);
        }
    }

    public static WebDriver getDriver()
    {
        if(driver.get() == null)
        {
            initDriver(BrowserType.CHROME);
        }
        return driver.get();
    }

    public static void quitDriver()
    {
        driver.get().quit();
        driver.remove();
    }
}
