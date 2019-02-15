package utils;

public class QueueModeChecker {
    private static final String PUSH = "push";

    public QueueModeChecker(){}

    public static boolean isPush() {
        return null == System.getProperty(PUSH) || System.getProperty(PUSH).equalsIgnoreCase("true");
    }
}
