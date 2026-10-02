package org.utilities;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait; // NEW IMPORT

public class JuiceShopEnvironment {

    private static GenericContainer<?> juiceShopContainer;

    public static void startEnvironment() {
        if (juiceShopContainer == null) {
            juiceShopContainer = new GenericContainer<>("bkimminich/juice-shop:latest")
                    .withExposedPorts(3000)
                    // THE FIX: Tell Testcontainers to ping the homepage until it gets a 200 OK
                    .waitingFor(Wait.forHttp("/").forStatusCode(200));

            juiceShopContainer.start();
        }
    }

    public static String getBaseUrl() {
        if (juiceShopContainer == null || !juiceShopContainer.isRunning()) {
            throw new IllegalStateException("Juice Shop Container is not running.");
        }
        String host = juiceShopContainer.getHost();
        Integer port = juiceShopContainer.getMappedPort(3000);
        return "http://" + host + ":" + port;
    }

    public static void stopEnvironment() {
        if (juiceShopContainer != null) {
            juiceShopContainer.stop();
        }
    }
}