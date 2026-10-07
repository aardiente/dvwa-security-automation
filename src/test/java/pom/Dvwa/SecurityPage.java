package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class SecurityPage extends BasePage
{
    public static final String securityEndpoint = "/security.php";

    // Locators
    private static final By SECURITY_LEVEL_DROPDOWN = By.name("security");
    private static final By SUBMIT_BUTTON = By.name("seclev_submit");
    private static final By TXT_INPUT_FIELD = By.cssSelector("input[type='text'], input[name='txt']");
    private static final By ACTION_SUBMIT_BUTTON = By.cssSelector("input[type='submit'], button[type='submit']");

    // PHPIDS & Security Text Locators
    private static final By SECURITY_STATUS_LABEL = By.xpath("//div[@class='vulnerable_code_area']//p[contains(text(), 'Security level is currently:')]");
    private static final By PHPIDS_STATUS_LABEL = By.xpath("//div[@class='vulnerable_code_area']//p[contains(text(), 'PHPIDS is currently:')]");
    private static final By PHPIDS_TOGGLE_BUTTON = By.name("phpids_submit");
    private static final By SIMULATE_ATTACK_LINK = By.linkText("Simulate attack");
    private static final By VIEW_IDS_LOG_LINK = By.linkText("View IDS log");

    public SecurityPage(WebDriver driver)
    {
        super(driver);
    }

    // Actions
    public void setSecurityLevel(SecurityLevel lvl)
    {
        Select dropdown = new Select(driver.findElement(SECURITY_LEVEL_DROPDOWN));
        dropdown.selectByValue(lvl.toString());
        driver.findElement(SUBMIT_BUTTON).click();
    }

    public void enterPayload(String payload)
    {
        var element = wait.until(ExpectedConditions.visibilityOfElementLocated(TXT_INPUT_FIELD));
        element.clear();
        element.sendKeys(payload);
    }

    public void clickSubmitPayload()
    {
        wait.until(ExpectedConditions.elementToBeClickable(ACTION_SUBMIT_BUTTON)).click();
    }

    public void clickTogglePhpIds()
    {
        wait.until(ExpectedConditions.elementToBeClickable(PHPIDS_TOGGLE_BUTTON)).click();
    }

    public void clickSimulateAttack()
    {
        wait.until(ExpectedConditions.elementToBeClickable(SIMULATE_ATTACK_LINK)).click();
    }

    public void clickViewIdsLog()
    {
        wait.until(ExpectedConditions.elementToBeClickable(VIEW_IDS_LOG_LINK)).click();
    }

    public String getCurrentSecurityLevelText()
    {
        var element = wait.until(ExpectedConditions.visibilityOfElementLocated(SECURITY_STATUS_LABEL));
        return element.getText();
    }

    public String getCurrentPhpIdsStatusText()
    {
        var element = wait.until(ExpectedConditions.visibilityOfElementLocated(PHPIDS_STATUS_LABEL));
        return element.getText();
    }

    public enum SecurityLevel
    {
        LOW("low"),
        MEDIUM("medium"),
        HIGH("high"),
        IMPOSSIBLE("impossible");

        private final String descriptor;

        SecurityLevel(String str)
        {
            descriptor = str;
        }

        public String getSecurityLevel() { return descriptor; }

        @Override
        public String toString() { return descriptor; }
    }
}
