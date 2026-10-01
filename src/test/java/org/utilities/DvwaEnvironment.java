package org.utilities;

import org.testcontainers.containers.GenericContainer;

public class DvwaEnvironment {

    // Defines the exact image we pulled earlier
    private static GenericContainer<?> dvwaContainer;

    public static void startEnvironment() {
        if (dvwaContainer == null) {
            dvwaContainer = new GenericContainer<>("vulnerables/web-dvwa:latest")
                    .withExposedPorts(80);
            dvwaContainer.start();
        }
    }

    public static String getBaseUrl() {
        if (dvwaContainer == null || !dvwaContainer.isRunning()) {
            throw new IllegalStateException("DVWA Container is not running.");
        }
        // Retrieves the dynamic local port Docker assigned
        String host = dvwaContainer.getHost();
        Integer port = dvwaContainer.getMappedPort(80);
        return "http://" + host + ":" + port;
    }

    public static void stopEnvironment() {
        if (dvwaContainer != null) {
            dvwaContainer.stop();
        }
    }
}