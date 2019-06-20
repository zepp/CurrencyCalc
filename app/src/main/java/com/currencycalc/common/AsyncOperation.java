package com.currencycalc.common;

import java.util.concurrent.Callable;

public class AsyncOperation<T> implements Runnable {
    private final Callable<T> callable;
    private final AsyncConsumer<T> consumer;

    public AsyncOperation(Callable<T> callable, AsyncConsumer<T> consumer) {
        this.callable = callable;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            consumer.accept(callable.call(), null);
        } catch (Exception e) {
            consumer.accept(null, e);
        }
    }
}
