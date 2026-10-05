package pom.JuiceShop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ShopDashboardPage extends BasePage
{

    // 💡 Locators
    private final By searchValueHeader = By.cssSelector("span[id='searchValue']");
    private final By firstProductCardTitle = By.cssSelector(".mat-card .item-name");

    public ShopDashboardPage(WebDriver driver)
    {
        super(driver);
    }

    /**
     * Fetches the reflecting text snippet at the top of a search result page.
     * Essential for validating Reflected XSS exploits.
     */
    public String getActiveSearchValueText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchValueHeader)).getText();
    }

    public String getFirstProductTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductCardTitle)).getText();
    }
}
