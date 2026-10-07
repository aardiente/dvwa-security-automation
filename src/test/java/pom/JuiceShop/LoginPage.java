package pom.JuiceShop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage
{

    // 💡 Locators (Targeting Angular/Material attributes safely)
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("loginButton");

    public LoginPage(WebDriver driver)
    {
        super(driver);
    }

    /**
     * Executes authentication sequence. Can accept valid payloads or SQLi patterns.
     */
    public void login(String emailPayload, String password)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(emailPayload);
        driver.findElement(passwordField).sendKeys(password);

        // Wait until Material design activates the form button context
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }
}
