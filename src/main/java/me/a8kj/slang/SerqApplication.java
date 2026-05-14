package me.a8kj.slang;

/**
 * Entry point of the Serq application.
 * Demonstrates usage of the {@link SerqAPI} singleton facade.
 *
 * @author a8kj7sea
 */
public final class SerqApplication {

    private static final String TARGET = "ngl_username";

    private SerqApplication() {
    }

    public static void main(String[] args) {

        SerqAPI api = SerqAPI.getInstance();

        sleep(1200);

        api.send(TARGET, "Direct singleton dispatch.1");

        sleep(200);

        api.sendFlow(TARGET, "Message delivered through fluent singleton flow1.");

        sleep(5000);
    }

    private static void sleep(long ms) {

        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}