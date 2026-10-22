
package utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScreenshotHelper {

    private static final Path SCREENSHOT_DIR = Path.of("target", "screenshots");

    private ScreenshotHelper() {}

    public static void capture(WebDriver driver) {
        ITestResult result = Reporter.getCurrentTestResult();

        if (result == null) {
            throw new IllegalStateException("ScreenshotHelper must be called from a TestNG test.");
        }

        String testName = result.getMethod().getMethodName();
        Path screenshotPath = SCREENSHOT_DIR.resolve(testName + ".png");

        try {
            Files.createDirectories(SCREENSHOT_DIR);
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(screenshotPath, screenshot);

            System.out.println("[SCREENSHOT] Saved: " + screenshotPath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save screenshot: " + testName, e);
        }
    }

    public static void captureResponse(WebDriver driver, HttpResponse<String> response) {
        String body = response.body() == null ? "" : response.body();

        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { background: #161616; color: #e5e5e5; padding: 24px; font-family: Consolas, monospace; }
                        h2 { color: #70cfff; }
                        pre { white-space: pre-wrap; overflow-wrap: anywhere; line-height: 1.5; }
                    </style>
                </head>
                <body>
                    <h2>HTTP Response</h2>
                    <p>Status: %d</p>
                    <p>URL: %s</p>
                    <hr>
                    <pre>%s</pre>
                </body>
                </html>
                """.formatted(response.statusCode(), escape(response.request().uri().toString()), escape(body));

        Path preview = null;

        try {
            preview = Files.createTempFile("http-response-", ".html");
            Files.writeString(preview, html, StandardCharsets.UTF_8);

            driver.get(preview.toUri().toString());
            capture(driver);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create HTTP response preview.", e);
        } finally {
            if (preview != null) {
                try {
                    Files.deleteIfExists(preview);
                } catch (IOException ignored) {}
            }
        }
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
}
