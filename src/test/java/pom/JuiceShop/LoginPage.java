
package pom.JuiceShop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private final By dismissButton = By.cssSelector("button.close-dialog");
    private final By welcomeBannerDismiss = By.cssSelector("a.close-dialog");
    private final By cookieDialog = By.cssSelector(".cc-window[aria-label='cookieconsent']");
    private final By cookieDismiss = By.cssSelector("a[aria-label='dismiss cookie message']");

    private final By accountMenu = By.id("navbarAccount");
    private final By loginMenuButton = By.id("navbarLoginButton");
    private final By logoutButton = By.id("navbarLogoutButton");

    private final By sideNavButton = By.cssSelector("button[aria-label='Open Sidenav']");
    private final By sideNavLogin = By.cssSelector("mat-sidenav a[aria-label='Go to login page']");
    private final By sideNavLogout = By.cssSelector("mat-sidenav a[aria-label='Logout']");

    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("loginButton");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void dismissInitialOverlays() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(dismissButton)).click();
        } catch (Exception ignored) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(welcomeBannerDismiss)).click();
        } catch (Exception ignored) {}

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-backdrop")));
        dismissCookieConsent();
    }

    private void dismissCookieConsent() {
        boolean cookieVisible = driver.findElements(cookieDialog).stream().anyMatch(WebElement::isDisplayed);
        if (!cookieVisible) return;

        wait.until(ExpectedConditions.elementToBeClickable(cookieDismiss)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(cookieDialog));
    }

    @Override
    public void navigateToLoginViaNavbar() {
        wait.until(ExpectedConditions.presenceOfElementLocated(accountMenu));

        if (driver.findElement(accountMenu).isDisplayed()) {
            wait.until(ExpectedConditions.elementToBeClickable(accountMenu)).click();
            wait.until(ExpectedConditions.elementToBeClickable(loginMenuButton)).click();
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(sideNavButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(sideNavLogin)).click();
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
    }

    public void login(String email, String password) {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailInput.clear();
        emailInput.sendKeys(email);

        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        dismissCookieConsent();
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public boolean isUserLoggedIn() {
        try {
            if (driver.findElement(accountMenu).isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(accountMenu)).click();
                return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton)).isDisplayed();
            }

            wait.until(ExpectedConditions.elementToBeClickable(sideNavButton)).click();
            return wait.until(ExpectedConditions.visibilityOfElementLocated(sideNavLogout)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
