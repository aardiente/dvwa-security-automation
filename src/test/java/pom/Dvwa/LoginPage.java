package pom.Dvwa;

import automation.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage
{
    public static final String loginEndpoint = "/login.php";
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
