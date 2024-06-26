package com.lucendar.common.types;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * 后端为 ByteBuffer 的 InputStream
 */
public class ByteBufferBackedInputStream extends InputStream {

    private final ByteBuffer buf;

    /**
     * 构造函数
     *
     * @param buf 作为后端的 ByteBuffer
     */
    public ByteBufferBackedInputStream(ByteBuffer buf) {
        this.buf = buf;
    }

    public int read() {
        if (!buf.hasRemaining()) {
            return -1;
        }
        return buf.get() & 0xFF;
    }

    public int read(byte[] bytes, int off, int len) {
        if (!buf.hasRemaining()) {
            return -1;
        }

        len = Math.min(len, buf.remaining());
        buf.get(bytes, off, len);
        return len;
    }
}
