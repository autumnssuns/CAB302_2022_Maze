package Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static String formatDateTime(LocalDateTime dateTime){
        return DATE_TIME_FORMATTER.format(dateTime);
    }
}
