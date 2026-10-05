package utilities;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait; // NEW IMPORT

public class JuiceShopEnvironment extends DockerContainer
{
   public JuiceShopEnvironment()
   {
       super("bkimminich/juice-shop:latest", 3000);
   }
}