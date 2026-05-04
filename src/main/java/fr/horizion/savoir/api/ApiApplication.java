package fr.horizion.savoir.api;

import java.util.concurrent.CountDownLatch;

public final class ApiApplication {

    private ApiApplication() {
    }

    public static void main(String[] args) throws Exception {
        int port = resolvePort(args);
        ApiServer server = new ApiServer(port);
        server.start();

        System.out.println("API disponible sur http://localhost:" + port + "/api");
        new CountDownLatch(1).await();
    }

    private static int resolvePort(String[] args) {
        if (args != null && args.length > 0) {
            return Integer.parseInt(args[0]);
        }

        String envPort = System.getenv("API_PORT");
        if (envPort != null && !envPort.isBlank()) {
            return Integer.parseInt(envPort);
        }

        return 8080;
    }
}