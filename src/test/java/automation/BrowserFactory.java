
package automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.Proxy;

public class BrowserFactory {

    private static final boolean IS_HEADLESS = true;
    private static final String headless = "--headless=new";
    private static final String windowSize = "--window-size=1920,1080";

    public static WebDriver createDriver(BrowserType brow) {
        switch (brow) {
            case CHROME:
                ChromeOptions chromeOp = new ChromeOptions();
                if (IS_HEADLESS) chromeOp.addArguments(headless);
                return new ChromeDriver(chromeOp);

            case FIREFOX:
                FirefoxOptions foxOp = new FirefoxOptions();
                if (IS_HEADLESS) foxOp.addArguments(headless);
                return new FirefoxDriver(foxOp);

            case EDGE:
                EdgeOptions edgeOp = new EdgeOptions();
                if (IS_HEADLESS) edgeOp.addArguments(headless);
                return new EdgeDriver(edgeOp);

            case SAFARI:
                SafariOptions safariOp = new SafariOptions();
                return new SafariDriver(safariOp);

            default:
                throw new IllegalArgumentException("Unsupported Browser: " + brow);
        }
    }

    public static WebDriver createZapProxy(String zapProxyAddress) {
        Proxy prox = new Proxy();
        prox.setHttpProxy(zapProxyAddress);
        prox.setSslProxy(zapProxyAddress);

        ChromeOptions options = new ChromeOptions();
        options.setProxy(prox);
        options.setAcceptInsecureCerts(true);

        if (IS_HEADLESS) {
            options.addArguments(headless);
            options.addArguments(windowSize);
        }

        options.addArguments("--proxy-bypass-list=<-loopback>");
        return new ChromeDriver(options);
    }
}
