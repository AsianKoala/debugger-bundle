package newteamcode.util;

public class Omodeclock {
    private static double lastMessage = System.currentTimeMillis();

    public static boolean isOk() {
        if(System.currentTimeMillis() - lastMessage > 1000) {
            lastMessage = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
