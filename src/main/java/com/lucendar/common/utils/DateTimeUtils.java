package com.lucendar.common.utils;

import info.gratour.common.error.ErrorWithCode;
import info.gratour.common.error.Errors;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class DateTimeUtils {

    public enum DateTimeFmt {
        ISO_OFFSET_DATE_TIME,
        CONVENIENT_DATE_TIME,
        CONVENIENT_DATE_TIME_WITH_MILLIS;

        public static DateTimeFmt detect(String dateTimeStr) {
            if (dateTimeStr.contains("T"))
                return DateTimeFmt.ISO_OFFSET_DATE_TIME;
            else {
                if (dateTimeStr.contains("."))
                    return DateTimeFmt.CONVENIENT_DATE_TIME_WITH_MILLIS;
                else
                    return DateTimeFmt.CONVENIENT_DATE_TIME;
            }
        }
    }

    public static ZoneOffset defaultZoneOffset() {
        Clock clock = Clock.systemDefaultZone();
        return clock.getZone().getRules().getOffset(clock.instant());
    }

    public static final ZoneOffset DEFAULT_ZONE_OFFSET = defaultZoneOffset();
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    public static final ZoneId ZONE_ID_Z = ZoneId.of("Z");
    public static final ZoneOffset ZONE_OFFSET_BEIJING = ZoneOffset.ofHours(8);

    public static final DateTimeFormatter CONVENIENT_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter CONVENIENT_DATETIME_FORMATTER_SHORT_YEAR =
            DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter DATETIME_FORMATTER_WITH_ZONE1 =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss x");

    public static final DateTimeFormatter FILE_NAME_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static final DateTimeFormatter HTTP_DATE_FORMATTER =
            DateTimeFormatter
                    .ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                    .withZone(ZoneId.of("GMT"));

    /**
     * Get ZoneId of specified id string.
     *
     * @return null if zoneId not found or invalid.
     */
    public static ZoneId zoneIdOf(String zoneId) {
        if (zoneId == null || zoneId.isEmpty())
            return null;

        try {
            return ZoneId.of(zoneId);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Get ZoneId of specified zone offset(by minutes).
     *
     * @return null if offset out of range.
     */
    public static ZoneId zoneIdOfOffset(int zoneOffsetMinutes) {
        try {
            return ZoneOffset.ofTotalSeconds(zoneOffsetMinutes * 60);
        } catch (Throwable t) {
            return null;
        }
    }

    public static ZoneId zoneIdOfOffsetHour(int zoneOffsetHours) {
        try {
            return ZoneOffset.ofTotalSeconds(zoneOffsetHours * 60 * 60);
        } catch (Throwable t) {
            return null;
        }
    }

    public static class CachedZoneOffset {
        public static final AtomicReference<ZoneOffset> cachedZoneOffset = new AtomicReference<>();

        public static void recheck() {
            cachedZoneOffset.set(defaultZoneOffset());
        }

        public static String millisToOffsetDateTimeString(long epochMillis) {
            return Instant.ofEpochMilli(epochMillis).atOffset(cachedZoneOffset.get())
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        public static OffsetDateTime parseDateTime(String value) {
            if (value.contains("T"))
                return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            else {
                if (value.contains("."))
                    return LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS)
                            .atOffset(cachedZoneOffset.get());
                else
                    return LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER)
                            .atOffset(cachedZoneOffset.get());
            }
        }

        public static long stringToMillis(String value) {
            return parseDateTime(value).toInstant().toEpochMilli();
        }
    }


    public static String convenientDateTimeFormat(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis)
                .atOffset(DEFAULT_ZONE_OFFSET)
                .format(CONVENIENT_DATETIME_FORMATTER);
    }

    public static String dateTimeToFileName(LocalDateTime dt) {
        return dt.format(FILE_NAME_DATETIME_FORMATTER);
    }

    public static String offsetDateTimeNowString() {
        return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String offsetDateTimeNowString(ZoneId zoneId) {
        return OffsetDateTime.now().atZoneSameInstant(zoneId).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * Output format:
     * 39h 45m 47.045s
     * -39h -45m -47.045s
     *
     * @param duration
     * @return
     */
    public static String humanReadableDuration(Duration duration) {
        return duration.toString().substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    public static String millisToOffsetDateTimeString(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis)
                .atOffset(defaultZoneOffset())
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static long offsetDateTimeStringToMillis(String s) {
        return OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant().toEpochMilli();
    }

    public static OffsetDateTime parseDateTime(String value) {
        if (value.contains("T"))
            return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        else {
            if (value.contains("."))
                return LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS)
                        .atOffset(DateTimeUtils.defaultZoneOffset());
            else
                return LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER)
                        .atOffset(DateTimeUtils.defaultZoneOffset());
        }
    }

    public static OffsetDateTime parseDateTime(String value, DateTimeFmt fmt) {
        switch (fmt) {
            case ISO_OFFSET_DATE_TIME:
                return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            case CONVENIENT_DATE_TIME:
                return LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER)
                        .atOffset(DateTimeUtils.defaultZoneOffset());
            case CONVENIENT_DATE_TIME_WITH_MILLIS:
                return LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS)
                        .atOffset(DateTimeUtils.defaultZoneOffset());

            default:
                throw new ErrorWithCode(Errors.INTERNAL_ERROR, String.format("Unhandled DateTimeFmt: %s", fmt));
        }
    }

    public static long stringToMillis(String value) {
        return parseDateTime(value).toInstant().toEpochMilli();
    }

    public static String millisToConvenientDateTimeString(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis)
                .atOffset(defaultZoneOffset())
                .format(DateTimeUtils.CONVENIENT_DATETIME_FORMATTER);
    }

    public static String convenientDateTimeStrNow() {
        return millisToConvenientDateTimeString(System.currentTimeMillis());
    }


    public static String millisToConvenientDateTimeStringWithMillis(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis)
                .atOffset(defaultZoneOffset())
                .format(DateTimeUtils.CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS);
    }

    public static String convenientDateTimeStrWithMillisNow() {
        return millisToConvenientDateTimeStringWithMillis(System.currentTimeMillis());
    }

    public static String epochMillisToBeijingOffsetDateTimeStr(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis)
                .atOffset(ZONE_OFFSET_BEIJING)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String millisToString(long millis, DateTimeFmt fmt) {
        switch (fmt) {
            case ISO_OFFSET_DATE_TIME:
                return millisToOffsetDateTimeString(millis);

            case CONVENIENT_DATE_TIME:
                return millisToConvenientDateTimeString(millis);

            case CONVENIENT_DATE_TIME_WITH_MILLIS:
                return millisToConvenientDateTimeStringWithMillis(millis);

            default:
                throw new ErrorWithCode(Errors.INTERNAL_ERROR, String.format("Unhandled DateTimeFmt: %s", fmt));
        }
    }

    public static LocalDate tryStringToDate(String s) {
        if (s != null) {
            try {
                return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Throwable t) {
                return null;
            }
        } else
            return null;
    }

    public static LocalDate stringToDate(String s) {
        if (s != null) {
            try {
                return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Throwable t) {
                return null;
            }
        } else
            return null;
    }

    public static class BeijingConv {

        public static OffsetDateTime millisToOdt(long epochMillis) {
            return Instant.ofEpochMilli(epochMillis)
                    .atOffset(ZONE_OFFSET_BEIJING);
        }

        public static OffsetDateTime millisToOdtBoxed(Long epochMillis) {
            if (epochMillis != null)
                return Instant.ofEpochMilli(epochMillis)
                        .atOffset(ZONE_OFFSET_BEIJING);
            else
                return null;
        }

        public static String millisToStrBoxed(Long epochMillis) {
            if (epochMillis != null)
                return millisToOdt(epochMillis).format(CONVENIENT_DATETIME_FORMATTER);
            else
                return null;
        }

        public static String millisToString(long epochMillis) {
            return millisToOdt(epochMillis)
                    .format(CONVENIENT_DATETIME_FORMATTER);
        }

        public static Long tryStringToMillis(String s) {
            if (s != null) {
                try {
                    return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER)
                            .toInstant(ZONE_OFFSET_BEIJING)
                            .toEpochMilli();
                } catch (Throwable t) {
                    return null;
                }
            } else
                return null;
        }

        public static Long strToMillis(String s) {
            if (s != null)
                return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli();
            else
                return null;
        }

        public static long stringToMillis(String s) {
            return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli();
        }

        public static String secondsToString(long epochSeconds) {
            return Instant.ofEpochSecond(epochSeconds).atOffset(ZONE_OFFSET_BEIJING).format(CONVENIENT_DATETIME_FORMATTER);
        }

        public static long stringToSeconds(String s) {
            return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER)
                    .toInstant(ZONE_OFFSET_BEIJING).toEpochMilli() / 1000L;
        }

        public static Long tryStringToSeconds(String s) {
            if (s != null) {
                try {
                    return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER)
                            .toInstant(ZONE_OFFSET_BEIJING).toEpochMilli() / 1000L;
                } catch (Throwable t) {
                    return null;
                }
            } else
                return null;
        }

        public static OffsetDateTime strToOdt(String s) {
            if (s != null)
                return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING);
            else
                return null;
        }

        public static OffsetDateTime tryStrToOdt(String s) {
            if (s != null) {
                try {
                    return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING);
                } catch (Throwable t) {
                    return null;
                }
            } else
                return null;
        }

        public static String odtToStr(OffsetDateTime odt) {
            if (odt != null)
                return CONVENIENT_DATETIME_FORMATTER.format(odt);
            else
                return null;
        }

        /**
         *
         * @param s
         * @return
         * @deprecated This method name is too long, use strToOdt instead. Please note that strToOdt accepts null argument
         * while stringToOffsetDatetime does not.
         */
        @Deprecated
        public static OffsetDateTime stringToOffsetDateTime(String s) {
            return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING);
        }

        /**
         *
         * @param s
         * @return
         * @deprecated This method name is too long, use tryStrToOdt instead.
         */
        @Deprecated
        public static OffsetDateTime tryStringToOffsetDateTime(String s) {
            if (s != null) {
                try {
                    return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING);
                } catch (Throwable t) {
                    return null;
                }
            } else
                return null;
        }

        public static LocalDateTime strToLdt(String s) {
            if (s != null)
                return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER);
            else
                return null;
        }

    }

}

