package net.pixlies.utils;

public class TimeUnit {

    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;
    public static final long MONTH = DAY * 30;
    public static final long YEAR = DAY * 365;

    private TimeUnit() {}

    public static long getDuration(String duration) {
        int multiplier = Integer.parseInt(duration.substring(0, duration.length() - 1));
        return switch (duration.substring(duration.length() - 1)) {
            case "s" -> SECOND * multiplier;
            case "m" -> MINUTE * multiplier;
            case "h" -> HOUR * multiplier;
            case "d" -> DAY * multiplier;
            case "w" -> WEEK * multiplier;
            case "M" -> MONTH * multiplier;
            case "y" -> YEAR * multiplier;
            default -> multiplier;
        };
    }

}
