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

    public enum SecurityLevel
    {
        LOW("low"),
        MEDIUM("medium"),
        HIGH("high");

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

