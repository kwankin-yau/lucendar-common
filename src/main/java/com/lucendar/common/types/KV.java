package com.lucendar.common.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Key value tuple
 */
public class KV {

    private final String key;
    private final Object value;

    public KV(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static KV of(String key, Object value) {
        return new KV(key, value);
    }

    public String key() {
        return key;
    }

    public Object value() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", KV.class.getSimpleName() + "[", "]")
                .add("key='" + key + "'")
                .add("value=" + value)
                .toString();
    }

    /**
     * Key value tuple list
     */
    public static class KVList {
        private final List<KV> list = new ArrayList<>();

        /**
         * Add key value tuple.
         *
         * @param key the key
         * @param value the value
         * @return this
         */
        public KVList add(@NonNull String key, @Nullable Object value) {
            if (value != null)
                list.add(KV.of(key, value));

            return this;
        }

        /**
         * Add key value tuple when the value is not null.
         *
         * @param key the key
         * @param value the value. If the `value` is null, the `add` operation will be ignored.
         * @return this
         */
        public KVList addNonNull(@NonNull String key, @Nullable Object value) {
            if (value != null)
                list.add(KV.of(key, value));

            return this;
        }

        /**
         * Make a KV array.
         *
         * @return KV array.
         */
        public KV[] toArray() {
            return list.toArray(new KV[0]);
        }
    }
}
