
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

    private static final boolean IS_HEADLESS = false;
    private static final String headless = "--headless=new";
    private static final String windowSize = "--window-size=1920,1080";
    private static final String zapOptions = "--proxy-bypass-list=<-loopback>";

    private static final String[] headOptions = {headless, windowSize, zapOptions};
    private static final String[] defaultOptions = {windowSize, zapOptions};

    public static WebDriver createDriver(BrowserType brow) {
        switch (brow) {
            case CHROME:
                ChromeOptions chromeOp = new ChromeOptions();
                chromeOp.addArguments(IS_HEADLESS ? headOptions : defaultOptions);
                return new ChromeDriver(chromeOp);

            case FIREFOX:
                FirefoxOptions foxOp = new FirefoxOptions();
                foxOp.addArguments(IS_HEADLESS ? headOptions : defaultOptions); // Requires changes doesnt use --headless=new
                return new FirefoxDriver(foxOp);

            case EDGE:
                EdgeOptions edgeOp = new EdgeOptions();
                edgeOp.addArguments(IS_HEADLESS ? headOptions : defaultOptions);
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

        options.addArguments(IS_HEADLESS ? headOptions : defaultOptions);

        return new ChromeDriver(options);
    }
}
