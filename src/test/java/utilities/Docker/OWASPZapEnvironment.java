package utilities.Docker;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class OWASPZapEnvironment extends DockerContainer
{
    private static final String ZAP_Image = "ghcr.io/zaproxy/zaproxy:stable";
    private static final int ZAP_Internal_Port = 8080;

    public OWASPZapEnvironment()
    {
        super(ZAP_Image, ZAP_Internal_Port);
    }

    @Override
    public void startEnvironment()
    {
        if(container != null)
            return;
        try
        {
            container = new GenericContainer<>(containerName)
                    .withNetwork(SHARED_NETWORK)
                    .withNetworkAliases(networkAlias)
                    .withExposedPorts(exposedPort)
                    .withCommand(
                            "zap.sh", "-daemon",
                            "-host", "0.0.0.0",
                            "-port", String.valueOf(exposedPort),
                            "-config", "api.addrs.addr.name=.*",
                            "-config", "api.addrs.addr.regex=true",
                            "-config", "api.disablekey=true"
                    )
                    .waitingFor(
                            Wait.forLogMessage(".*ZAP is now listening.*", 1)
                                    .withStartupTimeout(Duration.ofMinutes(2))
                    );

            container.start();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getProxyAddress()
    {
        if(container == null || !container.isRunning())
        {
            throw new IllegalStateException("Container: " + containerName + " is not running.");
        }
        return container.getHost() + ":" + container.getMappedPort(exposedPort);
    }

    public String getHost()
    {
        return container.getHost();
    }

    public int getMappedPort()
    {
        return container.getMappedPort(exposedPort);
    }
}