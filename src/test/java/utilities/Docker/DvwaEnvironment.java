package utilities.Docker;

public class DvwaEnvironment extends DockerContainer
{
   public DvwaEnvironment()
   {
       super("vulnerables/web-dvwa:latest", 80);
   }
}