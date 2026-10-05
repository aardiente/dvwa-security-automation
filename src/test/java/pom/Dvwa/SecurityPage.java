package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class SecurityPage extends BasePage
{
    // Locators
    private final By securityLevelDropdown = By.name("security");
    private final By submitButton = By.name("seclev_submit");

    public SecurityPage(WebDriver driver)
    {
        super(driver);
    }

    // Actions
    public void setSecurityLevel(String level) {
        Select dropdown = new Select(driver.findElement(securityLevelDropdown));
        dropdown.selectByValue(level.toLowerCase());
        driver.findElement(submitButton).click();
    }
}
