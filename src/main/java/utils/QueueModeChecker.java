package utils;

public class QueueModeChecker {
    private static final String MODE = "mode";

    public QueueModeChecker() {
    }

    public static boolean isPush() {
        return System.getProperty(MODE).equalsIgnoreCase("push");
    }
}
