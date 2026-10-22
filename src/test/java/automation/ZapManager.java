
package automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import utilities.Docker.OWASPZapEnvironment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class ZapManager {

    private static final ThreadLocal<OWASPZapEnvironment> zapEnvironment = new ThreadLocal<>();
    private static final ThreadLocal<ClientApi> zapClient = new ThreadLocal<>();
    private static final ThreadLocal<WebDriver> zapDriver = new ThreadLocal<>();
    private static final int DefaultTimeOut = 10;

    private ZapManager() {}

    public static void initZap() {
        if (zapEnvironment.get() == null) {
            OWASPZapEnvironment env = new OWASPZapEnvironment();
            env.startEnvironment();
            zapEnvironment.set(env);

            ClientApi client = new ClientApi(env.getHost(), env.getMappedPort(), null);
            zapClient.set(client);
        }
    }

    public static void initZapDriver() {
        if (zapEnvironment.get() == null) {
            initZap();
        }

        if (zapDriver.get() == null) {
            String proxyAddress = zapEnvironment.get().getProxyAddress();
            WebDriver webDriver = BrowserFactory.createZapProxy(proxyAddress);
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DefaultTimeOut));

            SlowMoListener listener = new SlowMoListener(webDriver);
            WebDriver decoratedDriver = new EventFiringDecorator<>(listener).decorate(webDriver);
            zapDriver.set(decoratedDriver);
        }
    }

    public static WebDriver getDriver() {
        if (zapDriver.get() == null) {
            initZapDriver();
        }
        return zapDriver.get();
    }

    public static void quitZapDriver() {
        if (zapDriver.get() != null) {
            zapDriver.get().quit();
            zapDriver.remove();
        }
    }

    public static ClientApi getZapClient() {
        if (zapClient.get() == null) {
            initZap();
        }
        return zapClient.get();
    }

    public static String getProxyAddress() {
        if (zapEnvironment.get() == null) {
            initZap();
        }
        return zapEnvironment.get().getProxyAddress();
    }

    public static void generateReport() {
        ClientApi client = zapClient.get();
        if (client == null) return;

        try {
            int recordsRemaining;
            do {
                Thread.sleep(1000);
                ApiResponseElement response = (ApiResponseElement) client.pscan.recordsToScan();
                recordsRemaining = Integer.parseInt(response.getValue());
            } while (recordsRemaining != 0);

            byte[] report = client.core.htmlreport();
            Path reportPath = Paths.get("target", "zap-report.html");
            Files.createDirectories(reportPath.getParent());
            Files.write(reportPath, report);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ZAP report.", e);
        }
    }

    public static void quitZap() {
        quitZapDriver();

        if (zapEnvironment.get() != null) {
            zapEnvironment.get().stopEnvironment();
            zapEnvironment.remove();
            zapClient.remove();
        }
    }
}
