package pom.JuiceShop;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver, Duration.ofSeconds(10)
        );
    }

    protected WebElement visible(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    protected void click(By locator) {
        wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        ).click();
    }

    protected void type(By locator, String value) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected boolean isVisible(By locator) {
        return driver.findElements(locator)
                .stream()
                .anyMatch(WebElement::isDisplayed);
    }

    public void dismissInitialOverlays() {
        dismissIfPresent(By.cssSelector(
                "button[aria-label='Close Welcome Banner'], " +
                        "button.close-dialog"
        ));

        dismissIfPresent(By.cssSelector(
                "a[aria-label='dismiss cookie message']"
        ));
    }

    private void dismissIfPresent(By locator) {
        for (WebElement element : driver.findElements(locator)) {
            if (element.isDisplayed() && element.isEnabled()) {
                element.click();
                break;
            }
        }
    }
}