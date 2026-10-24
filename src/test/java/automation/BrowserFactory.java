
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

    private static final String headless = "--headless=new";
    private static final String zapOptions = "--proxy-bypass-list=<-loopback>";

    private static String windowSize() {
        return "--window-size=" + AppConfig.viewportWidth() + "," + AppConfig.viewportHeight();
    }

    public static void setViewport(WebDriver driver) {
        driver.manage().window().setSize(AppConfig.viewport());
    }

    public static WebDriver createDriver(BrowserType brow) {
        switch (brow) {
            case CHROME:
                ChromeOptions chromeOp = new ChromeOptions();
                chromeOp.addArguments(windowSize(), zapOptions);

                if (AppConfig.isHeadless()) chromeOp.addArguments(headless);

                return new ChromeDriver(chromeOp);

            case FIREFOX:
                FirefoxOptions foxOp = new FirefoxOptions();
                foxOp.addArguments("--width=" + AppConfig.viewportWidth());
                foxOp.addArguments("--height=" + AppConfig.viewportHeight());

                if (AppConfig.isHeadless()) foxOp.addArguments("-headless");

                return new FirefoxDriver(foxOp);

            case EDGE:
                EdgeOptions edgeOp = new EdgeOptions();
                edgeOp.addArguments(windowSize(), zapOptions);

                if (AppConfig.isHeadless()) edgeOp.addArguments(headless);

                return new EdgeDriver(edgeOp);

            case SAFARI:
                if (AppConfig.isHeadless()) {
                    throw new IllegalArgumentException("Headless mode is not supported for Safari");
                }

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
        options.addArguments(windowSize(), zapOptions);

        if (AppConfig.isHeadless()) options.addArguments(headless);

        return new ChromeDriver(options);
    }
}
