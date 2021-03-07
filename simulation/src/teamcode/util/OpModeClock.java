package teamcode.util;

public class OpModeClock {
    private static double lastMessage = System.currentTimeMillis();

    public static boolean isOk() {
        if(System.currentTimeMillis() - lastMessage > 250) {
            lastMessage = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
