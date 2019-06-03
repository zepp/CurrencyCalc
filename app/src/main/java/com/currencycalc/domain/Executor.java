package com.currencycalc.domain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {
    private final static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static ExecutorService getExecutor() {
        return Executors.unconfigurableExecutorService(executor);
    }
}
