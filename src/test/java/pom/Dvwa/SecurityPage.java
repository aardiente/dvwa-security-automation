package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class SecurityPage extends BasePage
{
    public static final String securityEndpoint = "/security.php";
    // Locators
    private final By securityLevelDropdown = By.name("security");
    private final By submitButton = By.name("seclev_submit");

    public SecurityPage(WebDriver driver)
    {
        super(driver);
    }

    // Actions
    public void setSecurityLevel(SecurityLevel lvl) {
        Select dropdown = new Select(driver.findElement(securityLevelDropdown));
        dropdown.selectByValue(lvl.toString());
        driver.findElement(submitButton).click();
    }

    public enum SecurityLevel
    {
        LOW("low"),
        MEDIUM("medium"),
        HIGH("high");

        private final String descriptor;

        SecurityLevel (String str)
        {
            descriptor = str;
        }
        public String getSecurityLevel()
        {
            return descriptor;
        }
        @Override
        public String toString()
        {
            return descriptor;
        }
    }

}
