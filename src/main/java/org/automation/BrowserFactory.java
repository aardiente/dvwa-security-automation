package org.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class BrowserFactory
{
    public static WebDriver createDriver(BrowserType brow)
    {
        switch(brow)
        {
            case CHROME:
                ChromeOptions chromeOp = new ChromeOptions();
                return new ChromeDriver(chromeOp);
            case FIREFOX:
                FirefoxOptions foxOp = new FirefoxOptions();
                return new FirefoxDriver(foxOp);
            case EDGE:
                EdgeOptions edgeOp = new EdgeOptions();
                return new EdgeDriver(edgeOp);
            case SAFARI:
                SafariOptions safariOp = new SafariOptions();
                return new SafariDriver(safariOp);
            default:
                throw new IllegalArgumentException("Unsupported Browser: " + brow);
        }
    }
}
