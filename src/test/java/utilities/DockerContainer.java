package utilities;

import org.testcontainers.containers.GenericContainer;

public abstract class DockerContainer
{
    protected GenericContainer<?> container = null;
    protected String containerName = null;
    protected Integer exposedPort;

    protected DockerContainer(String name, Integer port)
    {
        containerName = name;
        exposedPort = port;
    }

    public void startEnvironment()
    {
        if(container == null)
        {
            container = new GenericContainer<>(containerName).withExposedPorts(exposedPort);
            container.start();
        }
    }

    public void stopEnvironment()
    {
        if(container != null && container.isRunning())
            container.stop();
    }

    public String getURL()
    {
        if(container == null || !container.isRunning())
            throw new IllegalStateException("Container: " + containerName + " is not running.");

        return "http://" + container.getHost() + ":" + container.getMappedPort(exposedPort);
    }
    public Integer getExposedPort()
    {
        return exposedPort;
    }

}
