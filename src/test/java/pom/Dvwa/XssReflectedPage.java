package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class XssReflectedPage extends BasePage {

    private static final String xssReflectedEndpoint = "vulnerabilities/xss_r/";
    // 💡 Static final locators that never change
    private static final By NAME_INPUT_FIELD = By.name("name");
    private static final By SUBMIT_BUTTON = By.cssSelector("input[value='Submit']");

    public XssReflectedPage(WebDriver driver) {
        super(driver);
    }

    public void injectPayload(String payload) {
        driver.findElement(NAME_INPUT_FIELD).clear();
        driver.findElement(NAME_INPUT_FIELD).sendKeys(payload);
        driver.findElement(SUBMIT_BUTTON).click();
    }
}

