package automation;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class SlowMoListener implements WebDriverListener {

    private final WebDriver driver;
    private final int delay = 250;

    public SlowMoListener(WebDriver driver) {
        this.driver = driver;
    }

    // Triggers automatically right before any .click() command
    @Override
    public void beforeClick(WebElement element) {
        highlightAndPause(element);
    }

    // Triggers automatically right before any .sendKeys() command
    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        highlightAndPause(element);
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
        }
    }
}