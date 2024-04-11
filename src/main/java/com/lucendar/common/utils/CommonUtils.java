package com.lucendar.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Common utilities
 */
public class CommonUtils {

    private static void internalGetFields(Class<?> clzz, List<Field> fields) {
        for (Field f : clzz.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers()))
                fields.add(f);
        }

        if (clzz.getSuperclass() != Object.class)
            internalGetFields(clzz.getSuperclass(), fields);
    }

    /**
     * Get instance fields(non-static) declarations of given class
     *
     * @param clzz the given class
     * @return Instance fields declarations
     */
    public static Field[] getInstanceFields(Class<?> clzz) {
        List<Field> r = new ArrayList<>();
        internalGetFields(clzz, r);

        return r.toArray(new Field[0]);
    }

    /**
     * A static Random instance
     */
    public static final Random RANDOM = new Random(System.nanoTime());

    /**
     * Generate random bytes.
     *
     * @param bytes the output random bytes
     */
    public static void randomBytes(byte[] bytes) {
        RANDOM.nextBytes(bytes);
    }

    /**
     * Generate random bytes in given length and format it as Base64 string.
     *
     * @param bytesSize bytes count
     * @return Base64 formatted string.
     */
    public static String randomBytesBase64(int bytesSize) {
        byte[] bytes = new byte[bytesSize];
        randomBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    /**
     * Generate 16 random bytes and format it as Base64 string.
     *
     * @return Base64 formatted string.
     */
    public static String randomBytesBase64() {
        return randomBytesBase64(16);
    }

    private static final String BASE_32 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Generate random bytes in given length and format it as Base32 string.
     *
     * @param len bytes count
     * @return Base32 formatted string
     */
    public static String randomBase32(int len) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            int idx = RANDOM.nextInt(32);
            str[i] = BASE_32.charAt(idx);
        }

        return new String(str);
    }

    private static final String BASE_10 = "0123456789";

    /**
     * Generate random bytes in given length and format it as decimal string.
     *
     * @param len bytes count
     * @return Decimal string.
     */
    public static String randomBase10(int len) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            int idx = RANDOM.nextInt(10);
            str[i] = BASE_10.charAt(idx);
        }

        return new String(str);
    }

    /**
     * Convert UUID to bytes array.
     *
     * @param uuid the given UUID.
     * @return bytes array.
     */
    public static byte[] uuidToBytes(UUID uuid) {
        byte[] r = new byte[16];
        ByteBuffer buff = ByteBuffer.wrap(r);
        buff.putLong(uuid.getMostSignificantBits());
        buff.putLong(uuid.getLeastSignificantBits());

        return r;
    }

    private static final Base64.Encoder UrlSafeBase64EncoderNoPadding =
            Base64.getUrlEncoder().withoutPadding();

    /**
     * Format UUID to base64 string, optional with padding.
     *
     * @param uuid        The given UUID.
     * @param withPadding Whether append padding to the string end if needed.
     * @return The formatted Base64 string.
     */
    public static String uuidToBase64(UUID uuid, boolean withPadding) {
        byte[] bytes = new byte[16];
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        Base64.Encoder base64 = withPadding ? Base64.getUrlEncoder() : UrlSafeBase64EncoderNoPadding;
        return base64.encodeToString(bytes);
    }

    /**
     * Format UUID to Base64 string, with padding if needed.
     *
     * @param uuid The given UUID.
     * @return The formatted Base64 string.
     */
    public static String uuidToBase64(UUID uuid) {
        return uuidToBase64(uuid, true);
    }

    /**
     * Format UUID to Base64 string without padding.
     *
     * @param uuid The given UUID.
     * @return The formatted Base64 string.
     */
    public static String uuidToBase64NoPadding(UUID uuid) {
        return uuidToBase64(uuid, false);
    }

    /**
     * Generate a random UUID and convert it to Base64 string with padding.
     *
     * @return The converted Base64 string.
     */
    public static String uuidBase64() {
        return uuidToBase64(UUID.randomUUID());
    }

    /**
     * Generate a random UUID and convert it to Base64 string without padding.
     *
     * @return The converted Base64 string.
     */
    public static String uuidBase64NoPadding() {
        return uuidToBase64(UUID.randomUUID(), false);
    }

    /**
     * Generate a random UUID and return the `toString()` result of the generated UUID.
     * @return The UUID string representation.
     */
    public static String randomUuidString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Compare two string in case-sensitive manner. Used in sort method.
     *
     * @param a String a.
     * @param b String b.
     * @return The compare result, 1 for a > b, -1 for a < b, 0 for a == b.
     */
    public static int stringCompare(String a, String b) {
        if (a != null) {
            if (b == null)
                return 1;
            else
                return a.compareTo(b);
        } else if (b != null)
            return -1;
        else
            return 0;
    }

    /**
     * Compare tow string in case-insensitive manner. Used in sort method.
     *
     * @param a String a.
     * @param b String b.
     * @return The compare result, 1 for a > b, -1 for a < b, 0 for a == b.
     */
    public static int stringCompareIgnoreCase(String a, String b) {
        if (a != null) {
            if (b == null)
                return 1;
            else
                return a.compareToIgnoreCase(b);
        } else if (b != null)
            return -1;
        else
            return 0;
    }

}
