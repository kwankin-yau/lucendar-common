package com.lucendar.common.types;

import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteBufferBackedOutputStream extends OutputStream {
    private final ByteBuffer buf;

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
