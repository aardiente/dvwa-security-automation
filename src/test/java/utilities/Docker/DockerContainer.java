package utilities.Docker;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

public abstract class DockerContainer
{
    protected static final Network SHARED_NETWORK = Network.newNetwork();

    protected GenericContainer<?> container = null;
    protected String containerName = null;
    protected Integer exposedPort;
    protected String networkAlias;

    protected DockerContainer(String name, Integer port)
    {
        containerName = name;
        exposedPort = port;
        networkAlias = name.replaceAll(".*/", "").replaceAll(":.*", "");
    }

    public void startEnvironment()
    {
        if(container == null)
        {
            container = new GenericContainer<>(containerName)
                    .withNetwork(SHARED_NETWORK)
                    .withNetworkAliases(networkAlias)
                    .withExposedPorts(exposedPort);
            container.start();
        }
    }

    public void stopEnvironment()
    {
        if(container != null && container.isRunning())
        {
            container.stop();
            container = null;
        }
    }

    public String getURL()
    {
        if(container == null || !container.isRunning())
            throw new IllegalStateException("Container: " + containerName + " is not running.");

        return "http://" + container.getHost() + ":" + container.getMappedPort(exposedPort);
    }

    public String getInternalURL()
    {
        if(container == null || !container.isRunning())
            throw new IllegalStateException("Container: " + containerName + " is not running.");

        return "http://" + networkAlias + ":" + exposedPort;
    }

    public Integer getExposedPort()
    {
        return exposedPort;
    }
}