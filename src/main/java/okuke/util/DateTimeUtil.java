package okuke.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateTimeUtil {
    private DateTimeUtil() {}

    // Accept common inputs:
    // - yyyy-MM-dd
    // - yyyy-MM-dd HHmm
    // - yyyy-MM-dd'T'HH:mm
    // - d/M/yyyy
    // - d/M/yyyy HHmm
    // - d-M-yyyy (and HHmm)
    // - d.M.yyyy (and HHmm)
    private static final DateTimeFormatter[] DATE_TIME_PATTERNS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,            // yyyy-MM-dd'T'HH:mm
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("d-M-yyyy HHmm"),
            DateTimeFormatter.ofPattern("d.M.yyyy HHmm")
    };

    private static final DateTimeFormatter[] DATE_ONLY_PATTERNS = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_LOCAL_DATE,                 // yyyy-MM-dd
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("d.M.yyyy")
    };

    /** Parse date/time flexibly. If only a date is given, returns at 00:00. */
    public static LocalDateTime parseFlexibleDateTime(String s) {
        String in = s.trim().replaceAll("\\s+", " ");
        for (DateTimeFormatter f : DATE_TIME_PATTERNS) {
            try { return LocalDateTime.parse(in, f); } catch (DateTimeParseException ignore) {}
        }
        for (DateTimeFormatter f : DATE_ONLY_PATTERNS) {
            try { return LocalDate.parse(in, f).atStartOfDay(); } catch (DateTimeParseException ignore) {}
        }
        throw new DateTimeParseException("Unrecognized date/time: " + s, s, 0);
    }

    /** Nice print: omit time if 00:00, otherwise include HH:mm. */
    public static String formatNice(LocalDateTime dt) {
        if (dt.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dt.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }
        return dt.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"));
    }
}
