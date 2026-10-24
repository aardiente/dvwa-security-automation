
package automation;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SlowMoListener implements WebDriverListener {

    private static final Logger log = LoggerFactory.getLogger("org.openqa.selenium");
    private final WebDriver driver;

    public SlowMoListener(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        log.info("Navigating to URL: {}", url);
    }

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        log.debug("Finding element: {}", locator);
    }

    @Override
    public void beforeClick(WebElement element) {
        log.info("Clicking element: {}", element);
        if (AppConfig.isSlowMoEnabled()) highlightAndPause(element);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        log.debug("Sending keys to element: {}", element);
        if (AppConfig.isSlowMoEnabled()) highlightAndPause(element);
    }

    @Override
    public void beforeAccept(Alert alert) {
        log.info("Accepting JavaScript alert");
        AppConfig.pauseForResults();
    }

    @Override
    public void beforeDismiss(Alert alert) {
        log.info("Dismissing JavaScript alert");
        AppConfig.pauseForResults();
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        log.error("Selenium error during [{}]: {}", method.getName(), e.getTargetException().getMessage());
    }

    private void highlightAndPause(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='4px solid red'", element);
            Thread.sleep(AppConfig.slowMoDelayMs());
            js.executeScript("arguments[0].style.border=''", element);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.debug("Skipped highlight on detached/stale element: {}", e.getMessage());
        }
    }
}
