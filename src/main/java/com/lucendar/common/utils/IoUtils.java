/*
 * Copyright (c) 2024 lucendar.com.
 * All rights reserved.
 */
package com.lucendar.common.utils;

import java.nio.ByteBuffer;

public class IoUtils {

    public static byte[] toByteArray(ByteBuffer buf) {
        byte[] result;
        if (buf.hasArray() && buf.arrayOffset() == 0
                && buf.capacity() == buf.remaining()) {
            result = buf.array();
        } else {
            result = new byte[buf.remaining()];
            if (buf.hasArray()) {
                System.arraycopy(buf.array(),
                        buf.arrayOffset() + buf.position(), result, 0,
                        result.length);
            } else {
                // Direct buffer
                ByteBuffer duplicate = buf.duplicate();
                duplicate.mark();
                duplicate.get(result);
                duplicate.reset();
            }
        }

        return result;
    }
}
