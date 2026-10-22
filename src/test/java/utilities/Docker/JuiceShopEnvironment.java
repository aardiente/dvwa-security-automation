package utilities.Docker;

public class JuiceShopEnvironment extends DockerContainer
{
   public JuiceShopEnvironment()
   {
       super("bkimminich/juice-shop:latest", 3000);
   }
}