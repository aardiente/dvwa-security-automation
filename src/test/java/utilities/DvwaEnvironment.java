package utilities;

import org.testcontainers.containers.GenericContainer;

public class DvwaEnvironment extends DockerContainer
{
   public DvwaEnvironment()
   {
       super("vulnerables/web-dvwa:latest", 80);
   }
}