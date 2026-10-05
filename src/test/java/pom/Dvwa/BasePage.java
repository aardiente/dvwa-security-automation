package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage
{
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 💡 Universal DVWA Sidebar Navigation Elements
    private final By logoutLink = By.linkText("Logout");
    private final By bruteForceMenu = By.linkText("Brute Force");
    private final By commandInjectionMenu = By.linkText("Command Injection");
    private final By csrfMenu = By.linkText("CSRF");
    private final By fileInclusionMenu = By.linkText("File Inclusion");
    private final By sqlInjectionMenu = By.linkText("SQL Injection");
    private final By xssReflectedMenu = By.linkText("XSS (Reflected)");
    private final By xssStoredMenu = By.linkText("XSS (Stored)");
    private final By dvwaSecurityMenu = By.linkText("DVWA Security");

    public void clickLogout()
    {
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    public void navigateToBruteForce()
    {
        wait.until(ExpectedConditions.elementToBeClickable(bruteForceMenu)).click();
    }

    public void navigateToCommandInjection()
    {
        wait.until(ExpectedConditions.elementToBeClickable(commandInjectionMenu)).click();
    }

    public void navigateToSqlInjection()
    {
        wait.until(ExpectedConditions.elementToBeClickable(sqlInjectionMenu)).click();
    }

    public void navigateToDvwaSecurity()
    {
        wait.until(ExpectedConditions.elementToBeClickable(dvwaSecurityMenu)).click();
    }

    public void navigateToXssReflected()
    {
        wait.until(ExpectedConditions.elementToBeClickable(xssReflectedMenu)).click();
    }

    public void navigateToXssStored()
    {
        wait.until(ExpectedConditions.elementToBeClickable(xssStoredMenu)).click();
    }
}
