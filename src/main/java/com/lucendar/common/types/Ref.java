package com.lucendar.common.types;

import java.util.StringJoiner;

public class Ref<V> {

    public V v;

    public Ref() {
    }

    public Ref(V v) {
        this.v = v;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    public V get() {
        return v;
    }

    public void set(V obj) {
        this.v = obj;
    }

    public boolean isNull() {
        return v == null;
    }

    public boolean nonNull() {
        return v != null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Ref.class.getSimpleName() + "[", "]")
                .add("v=" + v)
                .toString();
    }
}
