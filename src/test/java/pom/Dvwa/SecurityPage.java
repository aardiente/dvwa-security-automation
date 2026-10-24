
package pom.Dvwa;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class SecurityPage extends BasePage {

    public static final String securityEndpoint = "/security.php";

    private static final By SECURITY_LEVEL_DROPDOWN = By.name("security");
    private static final By SUBMIT_BUTTON = By.name("seclev_submit");
    private static final By TXT_INPUT_FIELD = By.cssSelector("input[type='text'], input[name='txt']");
    private static final By ACTION_SUBMIT_BUTTON = By.cssSelector("input[type='submit'], button[type='submit']");
    private static final By SECURITY_STATUS_LABEL = By.xpath("//p[contains(., 'Security level is currently:')]");
    private static final By PHPIDS_STATUS_LABEL = By.xpath("//p[contains(., 'PHPIDS is currently:')]");
    private static final By PHPIDS_TOGGLE_BUTTON = By.name("phpids_submit");
    private static final By SIMULATE_ATTACK_LINK = By.linkText("Simulate attack");
    private static final By VIEW_IDS_LOG_LINK = By.linkText("View IDS log");

    public SecurityPage(WebDriver driver) {
        super(driver);
    }

    public void setSecurityLevel(SecurityLevel lvl) {
        if (lvl == null) throw new IllegalArgumentException("Security level cannot be null");

        String expectedLevel = lvl.getSecurityLevel();
        wait.until(ExpectedConditions.urlContains(securityEndpoint));

        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(SECURITY_LEVEL_DROPDOWN));
        new Select(dropdown).selectByValue(expectedLevel);

        wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON)).click();

        try {
            wait.until(d -> {
                Cookie cookie = d.manage().getCookieNamed("security");
                return cookie != null && expectedLevel.equalsIgnoreCase(cookie.getValue());
            });
        } catch (TimeoutException e) {
            Cookie cookie = driver.manage().getCookieNamed("security");
            String actualLevel = cookie == null ? "<missing>" : cookie.getValue();

            throw new IllegalStateException(
                    "DVWA security level was not saved. Expected: " + expectedLevel +
                            ", actual: " + actualLevel + ", URL: " + driver.getCurrentUrl(), e
            );
        }

        System.out.println("DVWA security level verified: " + expectedLevel);
    }

    public void enterPayload(String payload) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(TXT_INPUT_FIELD));
        element.clear();
        element.sendKeys(payload);
    }

    public void clickSubmitPayload() {
        wait.until(ExpectedConditions.elementToBeClickable(ACTION_SUBMIT_BUTTON)).click();
    }

    public void clickTogglePhpIds() {
        wait.until(ExpectedConditions.elementToBeClickable(PHPIDS_TOGGLE_BUTTON)).click();
    }

    public void clickSimulateAttack() {
        wait.until(ExpectedConditions.elementToBeClickable(SIMULATE_ATTACK_LINK)).click();
    }

    public void clickViewIdsLog() {
        wait.until(ExpectedConditions.elementToBeClickable(VIEW_IDS_LOG_LINK)).click();
    }

    public String getCurrentSecurityLevelText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SECURITY_STATUS_LABEL)).getText();
    }

    public String getCurrentPhpIdsStatusText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PHPIDS_STATUS_LABEL)).getText();
    }

    public enum SecurityLevel {
        LOW("low"),
        MEDIUM("medium"),
        HIGH("high"),
        IMPOSSIBLE("impossible");

        private final String descriptor;

        SecurityLevel(String str) {
            descriptor = str;
        }

        public String getSecurityLevel() {
            return descriptor;
        }

        @Override
        public String toString() {
            return descriptor;
        }
    }
}
