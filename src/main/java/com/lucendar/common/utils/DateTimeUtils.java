package com.lucendar.common.utils;

import info.gratour.common.error.ErrorWithCode;
import info.gratour.common.error.Errors;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 日期时间辅助类
 */
public class DateTimeUtils {

    /**
     * 日期时间格式
     */
    public enum DateTimeFmt {

        /**
         * ISO OFFSET Date time 格式
         */
        ISO_OFFSET_DATE_TIME,

        /**
         * 常规格式：yyyy-MM-dd HH:mm:ss
         */
        CONVENIENT_DATE_TIME,

        /**
         * 带毫秒的常规格式：yyyy-MM-dd HH:mm:ss.SSS
         */
        CONVENIENT_DATE_TIME_WITH_MILLIS;

        public static DateTimeFmt detect(@NonNull String dateTimeStr) {
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
    @Nullable
    public static ZoneId zoneIdOf(@Nullable String zoneId) {
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
    @Nullable
    public static ZoneId zoneIdOfOffset(int zoneOffsetMinutes) {
        try {
            return ZoneOffset.ofTotalSeconds(zoneOffsetMinutes * 60);
        } catch (Throwable t) {
            return null;
        }
    }

    @Nullable
    public static ZoneId zoneIdOfOffsetHour(int zoneOffsetHours) {
        try {
            return ZoneOffset.ofTotalSeconds(zoneOffsetHours * 60 * 60);
        } catch (Throwable t) {
            return null;
        }
    }

    public static class CachedZoneOffset {
        private static final AtomicReference<ZoneOffset> cachedZoneOffset = new AtomicReference<>();

        public static void recheck() {
            cachedZoneOffset.set(defaultZoneOffset());
        }

        public static ZoneOffset get() {
            ZoneOffset r = cachedZoneOffset.get();
            if (r == null) {
                r = defaultZoneOffset();
                cachedZoneOffset.set(r);
            }

            return r;
        }

        public static String millisToOffsetDateTimeString(long epochMillis) {
            return Instant.ofEpochMilli(epochMillis).atOffset(cachedZoneOffset.get())
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        public static OffsetDateTime parseDateTime(@NonNull String value) {
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

        public static long stringToMillis(@NonNull String value) {
            return parseDateTime(value).toInstant().toEpochMilli();
        }
    }


    public static String convenientDateTimeFormat(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis)
                .atOffset(DEFAULT_ZONE_OFFSET)
                .format(CONVENIENT_DATETIME_FORMATTER);
    }

    public static String dateTimeToFileName(@NonNull LocalDateTime dt) {
        return dt.format(FILE_NAME_DATETIME_FORMATTER);
    }

    public static String offsetDateTimeNowString() {
        return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String offsetDateTimeNowString(@NonNull ZoneId zoneId) {
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
    public static String humanReadableDuration(@NonNull Duration duration) {
        return duration.toString().substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    public static String millisToOffsetDateTimeString(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis)
                .atOffset(defaultZoneOffset())
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static long offsetDateTimeStringToMillis(@NonNull String s) {
        return OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant().toEpochMilli();
    }

    public static OffsetDateTime parseDateTime(@NonNull String value) {
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

    public static OffsetDateTime parseDateTime(@NonNull String value, @NonNull DateTimeFmt fmt) {
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

    public static long stringToMillis(@NonNull String value) {
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

    public static String millisToString(long millis, @NonNull DateTimeFmt fmt) {
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

    public static LocalDate tryStringToDate(@NonNull String s) {
        if (s != null) {
            try {
                return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Throwable t) {
                return null;
            }
        } else
            return null;
    }

    @Nullable
    public static LocalDate stringToDate(@Nullable String s) {
        if (s != null) {
            try {
                return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Throwable t) {
                return null;
            }
        } else
            return null;
    }

    /**
     * 北京时区的日期时间惯例格式辅助类
     */
    public static class BeijingConv {

        /**
         * 返回给定字符串是否符合日期时间的惯例格式
         *
         * @param datetime 所要检查的日期时间字符串
         * @return 是否符合日期时间的惯例格式
         */
        public static boolean isValidStr(@NonNull String datetime) {
            if (datetime != null) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    LocalDateTime.parse(datetime, CONVENIENT_DATETIME_FORMATTER);
                } catch (DateTimeParseException t) {
                    return false;
                }

                return true;
            } else
                return false;
        }


        /**
         * 验证给定字符串是否符合日期时间的惯例格式，不符合时，抛出异常
         *
         * @param datetime 所要检查的日期时间字符串
         * @throws DateTimeParseException 如果格式不符，则抛出此异常
         */
        public static void validateStr(@NonNull String datetime) throws
                DateTimeParseException {
            if (datetime == null)
                throw new NullPointerException("datetime");

            //noinspection ResultOfMethodCallIgnored
            LocalDateTime.parse(datetime, CONVENIENT_DATETIME_FORMATTER);
        }

        public static OffsetDateTime millisToOdt(long epochMillis) {
            return Instant.ofEpochMilli(epochMillis)
                    .atOffset(ZONE_OFFSET_BEIJING);
        }

        @Nullable
        public static OffsetDateTime millisToOdtBoxed(@Nullable Long epochMillis) {
            if (epochMillis != null)
                return Instant.ofEpochMilli(epochMillis)
                        .atOffset(ZONE_OFFSET_BEIJING);
            else
                return null;
        }

        @Nullable
        public static String millisToStrBoxed(@Nullable Long epochMillis) {
            if (epochMillis != null)
                return millisToOdt(epochMillis).format(CONVENIENT_DATETIME_FORMATTER);
            else
                return null;
        }

        public static String millisToString(long epochMillis) {
            return millisToOdt(epochMillis)
                    .format(CONVENIENT_DATETIME_FORMATTER);
        }

        public static String nowString() {
            return millisToString(System.currentTimeMillis());
        }

        @Nullable
        public static Long tryStringToMillis(@Nullable String s) {
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

        @Nullable
        public static Long strToMillis(@Nullable String s) {
            if (s != null && !s.isEmpty())
                return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli();
            else
                return null;
        }

        public static long stringToMillis(@NonNull String s) {
            return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli();
        }

        public static String secondsToString(long epochSeconds) {
            return Instant.ofEpochSecond(epochSeconds).atOffset(ZONE_OFFSET_BEIJING).format(CONVENIENT_DATETIME_FORMATTER);
        }

        public static long stringToSeconds(@NonNull String s) {
            return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER)
                    .toInstant(ZONE_OFFSET_BEIJING).toEpochMilli() / 1000L;
        }

        @Nullable
        public static Long tryStringToSeconds(@Nullable String s) {
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

        @Nullable
        public static OffsetDateTime strToOdt(@Nullable String s) {
            if (s != null)
                return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING);
            else
                return null;
        }

        @Nullable
        public static OffsetDateTime tryStrToOdt(@Nullable String s) {
            if (s != null) {
                try {
                    return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING);
                } catch (Throwable t) {
                    return null;
                }
            } else
                return null;
        }

        @Nullable
        public static String odtToStr(@Nullable OffsetDateTime odt) {
            if (odt != null)
                return CONVENIENT_DATETIME_FORMATTER.format(odt.atZoneSameInstant(ZONE_OFFSET_BEIJING));
            else
                return null;
        }

        /**
         * @param s
         * @return
         * @deprecated This method name is too long, use strToOdt instead. Please note that strToOdt accepts null argument
         * while stringToOffsetDatetime does not.
         */
        @Deprecated
        public static OffsetDateTime stringToOffsetDateTime(@NonNull String s) {
            return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING);
        }

        /**
         * @param s
         * @return
         * @deprecated This method name is too long, use tryStrToOdt instead.
         */
        @Deprecated
        @Nullable
        public static OffsetDateTime tryStringToOffsetDateTime(@Nullable String s) {
            if (s != null) {
                try {
                    return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING);
                } catch (Throwable t) {
                    return null;
                }
            } else
                return null;
        }

        @Nullable
        public static LocalDateTime strToLdt(@Nullable String s) {
            if (s != null)
                return LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER);
            else
                return null;
        }

        public static OffsetDateTime tsToOdt(@Nullable Timestamp ts) {
            if (ts != null)
                return OffsetDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZONE_OFFSET_BEIJING);
            else
                return null;
        }

    }

}

