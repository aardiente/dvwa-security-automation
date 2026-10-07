package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
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
    private final By cspBypassMenu = By.linkText("CSP Bypass");
    private final By sqlInjectionBlindMenu = By.linkText("SQL Injection (Blind)");
    private final By weakSessionMenu = By.linkText("Weak Session IDs");
    private final By javascriptMenu = By.linkText("JavaScript");
    private final By phpInfoMenu = By.linkText("PHP Info");
    private final By aboutMenu = By.linkText("About");

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

    public void navigateToCsrf()
    {
        wait.until(ExpectedConditions.elementToBeClickable(csrfMenu)).click();
    }

    public void navigateToFileInclusion()
    {
        wait.until(ExpectedConditions.elementToBeClickable(fileInclusionMenu)).click();
    }

    public void navigateToCspBypass()
    {
        wait.until(ExpectedConditions.elementToBeClickable(cspBypassMenu)).click();
    }

    public void navigateToSqlInjectionBlind()
    {
        wait.until(ExpectedConditions.elementToBeClickable(sqlInjectionBlindMenu)).click();
    }

    public void navigateToWeakSessionIds()
    {
        wait.until(ExpectedConditions.elementToBeClickable(weakSessionMenu)).click();
    }

    public void navigateToJavaScript()
    {
        wait.until(ExpectedConditions.elementToBeClickable(javascriptMenu)).click();
    }

    public void navigateToPhpInfo()
    {
        wait.until(ExpectedConditions.elementToBeClickable(phpInfoMenu)).click();
    }

    public void navigateToAbout()
    {
        wait.until(ExpectedConditions.elementToBeClickable(aboutMenu)).click();
    }

    public void waitForPageLoad(String url)
    {
        wait.until(ExpectedConditions.urlContains(url));
    }
}
