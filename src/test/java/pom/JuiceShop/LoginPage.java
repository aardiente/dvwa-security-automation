package pom.JuiceShop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage
{
    private final By dismissButton = By.cssSelector("button.close-dialog");
    private final By welcomeBannerDismiss = By.cssSelector("a.close-dialog");
    private final By accountMenu = By.cssSelector("button#navbarAccount");
    private final By loginMenuButton = By.cssSelector("button#navbarLoginButton");
    private final By emailField = By.cssSelector("input#email");
    private final By passwordField = By.cssSelector("input#password");
    private final By loginButton = By.cssSelector("button#loginButton");
    private final By logoutButton = By.cssSelector("button#navbarLogoutButton");

    public LoginPage(WebDriver driver)
    {
        super(driver);
    }

    public void navigateToLoginViaNavbar()
    {
        wait.until(ExpectedConditions.elementToBeClickable(accountMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(loginMenuButton)).click();
    }

    public void login(String email, String password)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).clear();
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    public boolean isUserLoggedIn()
    {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(accountMenu)).click();
            WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
            return logout.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}