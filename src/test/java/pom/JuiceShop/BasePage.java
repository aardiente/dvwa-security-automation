
package pom.JuiceShop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    private final By dismissBannerButton = By.cssSelector("button[aria-label='Close Welcome Banner']");
    private final By acceptCookiesButton = By.cssSelector("a[aria-label='dismiss cookie message']");
    private final By navAccountMenu = By.id("navbarAccount");
    private final By navLoginButton = By.id("navbarLoginButton");
    private final By searchIcon = By.className("mat-search_icon");
    private final By searchBarInput = By.id("mat-input-0");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    public void dismissInitialOverlays() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(dismissBannerButton)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-backdrop")));
        } catch (Exception ignored) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesButton)).click();
        } catch (Exception ignored) {}
    }

    public void navigateToLoginViaNavbar() {
        wait.until(ExpectedConditions.elementToBeClickable(navAccountMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(navLoginButton)).click();
    }

    public void searchForProduct(String keyword) {
        wait.until(ExpectedConditions.elementToBeClickable(searchIcon)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBarInput)).sendKeys(keyword + "\n");
    }
}
