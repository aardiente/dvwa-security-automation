
package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Locale;

public class SqlInjectionPage extends BasePage {

    public static final String endpoint = "/vulnerabilities/sqli/";

    private final By idInputField = By.name("id");
    private final By submitButton = By.name("Submit");
    private final By resultRecords = By.cssSelector(".vulnerable_code_area pre");

    public SqlInjectionPage(WebDriver driver) {
        super(driver);
    }

    public void injectPayload(String payload) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(idInputField));
        input.clear();
        input.sendKeys(payload);

        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        wait.until(ExpectedConditions.urlContains("id="));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public int getResultCount() {
        List<WebElement> results = driver.findElements(resultRecords);
        int count = 0;

        for (WebElement result : results) {
            String text = result.getText();

            if (text.contains("First name:") && text.contains("Surname:")) {
                count++;
            }
        }

        return count;
    }

    public boolean hasSqlError() {
        String response = getResponseText().toLowerCase(Locale.ROOT);

        return response.contains("sql syntax")
                || response.contains("unknown column")
                || response.contains("mysqli error")
                || response.contains("sqlstate");
    }

    public String getResponseText() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body"))).getText();
    }

    public String getInjectionResultText() {
        return driver.findElements(resultRecords).stream()
                .map(WebElement::getText)
                .reduce("", (a, b) -> a + "\n" + b);
    }
}
