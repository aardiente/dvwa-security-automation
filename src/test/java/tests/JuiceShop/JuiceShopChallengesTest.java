package tests.JuiceShop;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import automation.ZapManager;
import pom.JuiceShop.LoginPage;
import pom.JuiceShop.SearchPage;

import java.time.Duration;

public class JuiceShopChallengesTest extends JuiceShopBaseTest
{
    @Test(priority = 1)
    public void testSqlInjectionLoginBypass()
    {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.dismissInitialOverlays();
        loginPage.navigateToLoginViaNavbar();

        // Execute the classic Juice Shop SQL Injection challenge: admin login bypass
        loginPage.login("admin@juice-sh.op'--", "anything");

        // Verify redirect to search and successful administrative session
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/search"));

        Assert.assertTrue(loginPage.isUserLoggedIn(), "SQL Injection authentication bypass failed: user is not logged in.");
    }

    @Test(priority = 2)
    public void testSearchInputEncoding() {

        var driver = ZapManager.getDriver();
        driver.get(baseUrl);

        SearchPage searchPage = new SearchPage(driver);
        searchPage.dismissInitialOverlays();

        String payload = "<b>SecurityProbe</b>";

        searchPage.search(payload);

        String renderedValue = searchPage.getSearchValue();

        Assert.assertTrue(
                renderedValue.contains("SecurityProbe"),
                "Search did not reflect the expected test marker."
        );

        Object injectedElementCount =
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript(
                                "return document.querySelectorAll(" +
                                        "'#searchValue b').length;"
                        );

        Assert.assertEquals(
                ((Number) injectedElementCount).intValue(),
                0,
                "Search value was interpreted as HTML."
        );
    }

}