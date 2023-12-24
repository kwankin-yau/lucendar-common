package com.lucendar.common.types;

public class LazyVar<T> {

    public interface VarInitializer<T> {
        T init();
    }

    private final VarInitializer<T> initializer;
    private volatile T value;
    public LazyVar(VarInitializer<T> initializer) {
        this.initializer = initializer;
    }

    public T get() {
        if (this.value == null) {
            synchronized (this) {
                if (this.value == null)
                    this.value = this.initializer.init();
            }
        }

        return this.value;
    }

}
