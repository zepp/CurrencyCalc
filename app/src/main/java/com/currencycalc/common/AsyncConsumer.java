package com.currencycalc.common;

@FunctionalInterface
public interface AsyncConsumer<T> {
    void accept(T t, Exception e);
}
