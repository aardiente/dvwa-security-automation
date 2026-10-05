package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage
{
    public final String loginEndpoint = "/login.php";
    // 💡 Locators
    private final By usernameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.name("Login");

    public LoginPage(WebDriver driver)
    {
        super(driver);
    }

    // 💡 Actions
    public void login(String username, String password)
    {
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    public WebDriver getDriver()
    {
        return this.driver;
    }
}
