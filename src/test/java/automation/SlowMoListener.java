package automation;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class SlowMoListener implements WebDriverListener {

    // Matches the "org.openqa.selenium" logger in log4j2.xml -> writes to logs/selenium.log
    private static final Logger log = LoggerFactory.getLogger("org.openqa.selenium");

    private final WebDriver driver;
    private final int delay = 250;

    public SlowMoListener(WebDriver driver) {
        this.driver = driver;
    }

    // Logs every URL navigation automatically
    @Override
    public void beforeGet(WebDriver driver, String url) {
        log.info("Navigating to URL: {}", url);
    }

    // Logs element lookups automatically
    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        log.debug("Finding element: {}", locator);
    }

    // Triggers automatically right before any .click() command
    @Override
    public void beforeClick(WebElement element) {
        log.info("Clicking element: {}", element);
        highlightAndPause(element);
    }

    // Triggers automatically right before any .sendKeys() command
    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        log.info("Sending keys {} to element: {}", Arrays.toString(keysToSend), element);
        highlightAndPause(element);
    }

    // Logs any Selenium exception (NoSuchElement, Timeout, StaleElement, etc.) automatically
    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        log.error("Selenium error during [{}]: {}", method.getName(), e.getTargetException().getMessage());
    }

    private void highlightAndPause(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Draw a thick red border around the target element
            js.executeScript("arguments[0].style.border='4px solid red'", element);

            // Pause based on the set delay (ms) 250 prevents race conditions - 1500 for visual verification
            Thread.sleep(delay);

            // Remove the border so the page looks normal again
            js.executeScript("arguments[0].style.border=''", element);

        } catch (Exception e) {
            // Failsafe in case an element disappears from the screen
            log.debug("Skipped highlight on detached/stale element: {}", e.getMessage());
        }
    }
}