package com.lucendar.common.utils;

import com.lucendar.common.types.OsType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CommonUtils {

    private static void internalGetFields(Class<?> clzz, List<Field> fields) {
        for (Field f : clzz.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers()))
                fields.add(f);
        }

        if (clzz.getSuperclass() != Object.class)
            internalGetFields(clzz.getSuperclass(), fields);
    }

    public static Field[] getInstanceFields(Class<?> clzz) {
        List<Field> r = new ArrayList<>();
        internalGetFields(clzz, r);

        return r.toArray(new Field[0]);
    }

    public static final Random RANDOM = new Random(System.nanoTime());

    public static void randomBytes(byte[] bytes) {
        RANDOM.nextBytes(bytes);
    }

    public static String randomBytesBase64(int bytesSize) {
        byte[] bytes = new byte[bytesSize];
        randomBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    public static String randomBytesBase64() {
        return randomBytesBase64(16);
    }

    private static final String BASE_32 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String randomBase32(int len) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            int idx = RANDOM.nextInt(32);
            str[i] = BASE_32.charAt(idx);
        }

        return new String(str);
    }

    private static final String BASE_10 = "0123456789";

    public static String randomBase10(int len) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            int idx = RANDOM.nextInt(32);
            str[i] = BASE_10.charAt(idx);
        }

        return new String(str);
    }

    public static byte[] uuidToBytes(UUID uuid) {
        byte[] r = new byte[16];
        ByteBuffer buff = ByteBuffer.wrap(r);
        buff.putLong(uuid.getMostSignificantBits());
        buff.putLong(uuid.getLeastSignificantBits());

        return r;
    }

    public static String randomUuidString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

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
