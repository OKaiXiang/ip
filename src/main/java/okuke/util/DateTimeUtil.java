package okuke.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility functions for parsing and formatting date/time inputs.
 * Supports multiple common input formats and "nice" display output.
 */

public final class DateTimeUtil {
    private DateTimeUtil() {}

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

    /**
     * Parses a date/time string using several common patterns.
     * If only a date is provided, returns the start of day (00:00).
     * <p>Supported inputs include:
     * <ul>
     *   <li>yyyy-MM-dd</li>
     *   <li>yyyy-MM-dd HHmm</li>
     *   <li>yyyy-MM-dd'T'HH:mm</li>
     *   <li>d/M/yyyy (and HHmm)</li>
     *   <li>d-M-yyyy (and HHmm)</li>
     *   <li>d.M.yyyy (and HHmm)</li>
     * </ul>
     *
     * @param s the input string to parse
     * @return parsed LocalDateTime (at 00:00 if time omitted)
     * @throws java.time.format.DateTimeParseException if none of the patterns match
     */
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

    /**
     * Formats a LocalDateTime for user-friendly display.
     * If the time is exactly midnight, omits the time component.
     * Examples: "Aug 06 2025" or "Aug 06 2025 14:00".
     *
     * @param dt the date/time to format
     * @return a human-friendly formatted string
     */
    public static String formatNice(LocalDateTime dt) {
        if (dt.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dt.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }
        return dt.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"));
    }
}
