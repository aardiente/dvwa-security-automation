package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class SqlInjectionPage extends BasePage
{

    // 💡 Locators
    private final By idInputField = By.name("id");
    private final By submitButton = By.name("Submit");
    private final By rawOutputContainer = By.xpath("//div[@class='vulnerable_code_area']//pre");

    public SqlInjectionPage(WebDriver driver)
    {
        super(driver);
    }

    // 💡 Actions
    public void injectPayload(String payload)
    {
        driver.findElement(idInputField).clear();
        driver.findElement(idInputField).sendKeys(payload);
        driver.findElement(submitButton).click();
    }

    public String getInjectionResultText()
    {
        try {
            return driver.findElement(rawOutputContainer).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
