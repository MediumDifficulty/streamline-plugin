package yes.mediumdifficulty.streamline.utils;

public class Uuid {
    public static String fullToTrimmed(String fullUuid) {
        return fullUuid.replaceAll("-", "");
    }
    
    public static String trimmedToFull(String trimmedUuid) {
        return trimmedUuid.substring(0, 8) + "-" + trimmedUuid.substring(8, 12) + "-" + trimmedUuid.substring(12, 16) + "-" + trimmedUuid.substring(16, 20) + "-" + trimmedUuid.substring(20);
    }
}
