package com.lucendar.common.types;

public class LazyVar<T> {

    public interface VarInitializer<T> {
        T init();
    }

    private final VarInitializer<T> initializer;
    private T value;
    public LazyVar(VarInitializer<T> initializer) {
        this.initializer = initializer;
    }

    public synchronized T get() {
        if (this.value == null)
            this.value = this.initializer.init();

        return this.value;
    }

}
