package io;

/**
 * Simple util for logging.
 */
@SuppressWarnings("unused")
public class Logger {
    private static boolean isEnabled = true;

    public static void log(String tag, String msg) {
        if (isEnabled) {
            System.out.printf("[%s] %s\n", tag, msg);
        }
    }

    public static void enable() {
        isEnabled = true;
    }

    public static void disable() {
        isEnabled = false;
    }
}
