package utilities.Logs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class SeleniumActionLogger implements WebDriverListener {

    // Matches the "org.openqa.selenium" logger in log4j2.xml -> writes to logs/selenium.log
    private static final Logger log = LoggerFactory.getLogger("org.openqa.selenium");

    @Override
    public void beforeGet(WebDriver driver, String url) {
        log.info("Navigating to URL: {}", url);
    }

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        log.debug("Locating element: {}", locator);
    }

    @Override
    public void beforeClick(WebElement element) {
        log.info("Clicking element: {}", element);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        log.info("Sending keys {} to element: {}", Arrays.toString(keysToSend), element);
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        log.error("Selenium error during [{}]: {}", method.getName(), e.getTargetException().getMessage());
    }
}
