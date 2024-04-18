package com.lucendar.common.types;

import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * 后端为 ByteBuffer 的 OutputStream
 */
public class ByteBufferBackedOutputStream extends OutputStream {
    private final ByteBuffer buf;

    /**
     * 构造函数
     * @param buf 作为后端的 ByteBuffer
     */
    public ByteBufferBackedOutputStream(ByteBuffer buf) {
        this.buf = buf;
    }

    public void write(int b) {
        buf.put((byte) b);
    }

    public void write(byte[] bytes, int off, int len) {
        buf.put(bytes, off, len);
    }

}
