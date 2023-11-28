package com.lucendar.common.utils;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;

public class StringUtils {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String nullAsEmpty(String s) {
        return s == null ? "" : s;
    }

    public static <T> String mkString(T[] arr, String start, String sep, String end) {
        if (arr != null) {
            StringBuilder str = new StringBuilder();
            str.append(start);
            for (int i = 0; i < arr.length; i++) {
                T item = arr[i];
                if (i > 0)
                    str.append(sep);
                if (item != null)
                    str.append(item);
                else
                    str.append("null");
            }
            str.append(end);

            return str.toString();
        } else
            return null;
    }

    public static <T> String arrayToString(T[] arr) {
        return mkString(arr, "[", ",", "]");
    }

    public static Boolean tryParseBool(String s) {
        try {
            return Boolean.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Short tryParseShort(String s) {
        try {
            return Short.parseShort(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long tryParseLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal tryParseDecimal(String s) {
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Float tryParseFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double tryParseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static LocalDate tryParseLocalDate(String value) {
        try {
            return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDateTime tryParseLocalDateTime(String value) {
        try {
            if (value.contains("T"))
                return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            else {
                if (value.contains("."))
                    return LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS);
                else
                    return LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER);
            }
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static OffsetDateTime tryParseOffsetDateTime(String value) {
        try {
            return DateTimeUtils.parseDateTime(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }



    public static int strLen(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            if (b == 0)
                return i;

        }

        return bytes.length;
    }

    public static int strLen(byte[] bytes, int maxLen) {
        int m = Math.min(maxLen, bytes.length);

        for (int i = 0; i < m; i++) {
            byte b = bytes[i];
            if (b == 0)
                return i;
        }

        return m;
    }

    public static String strMaxLen(byte[] bytes, int offset, int maxLen, Charset charset) {
        int l = maxLen;
        for (int i = 0; i < maxLen; i++) {
            byte b = bytes[i + offset];
            if (b == 0) {
                l = i;
                break;
            }
        }

        return new String(bytes, offset, l, charset);
    }

    public static String cStr(byte[] bytes, int maxLen) {
        if (bytes == null)
            return null;

        int l = strLen(bytes, maxLen);
        return new String(bytes, 0, l);
    }

    public static String cStr(byte[] bytes) {
        if (bytes != null)
            return cStr(bytes, bytes.length);
        else
            return null;
    }

    public static String cStr(byte[] bytes, int maxLen, Charset charset) {
        if (bytes == null)
            return null;

        int l = strLen(bytes, maxLen);
        return new String(bytes, 0, l, charset);
    }

    public static String cStr(byte[] bytes, Charset charset) {
        if (bytes != null)
            return cStr(bytes, bytes.length, charset);
        else
            return null;
    }

    public static String removeLeadingZero(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c != '0') {
                if (i == 0)
                    return value;
                else
                    return value.substring(i);
            }

        }

        return "";
    }

    public static byte[] urlSafeBase64NoPadding(String s) {
        int pad = s.length() % 4;
        String b64;
        switch (pad) {
            case 3:
                b64 = s + "=";
                break;

            case 2:
                b64 = s + "==";
                break;

            case 1:
                b64 = s + "===";
                break;

            default:
                b64 = s;
        }

        return Base64.getUrlDecoder().decode(b64);
    }

    public static String urlSafeBase64NoPadding(byte[] b) {
        String r = Base64.getUrlEncoder().encodeToString(b);
        int idx = r.indexOf('=');
        if (idx >= 0)
            return r.substring(0, idx);
        else
            return r;
    }

    public static String intToHex(int value, int minLen, boolean prepend0x) {
        String r = Integer.toHexString(value);
        if (r.length() < minLen)
            r = org.apache.commons.lang3.StringUtils.leftPad(r, minLen, '0');

        if (prepend0x)
            r = "0x" + r;

        return r;
    }

    public static String intToHex(int value, int minLen) {
        return intToHex(value, minLen, false);
    }
}
