package com.emailserver.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorManager {
    // Max threads that can be executed into Executor thread pool
    private static final int MAX_THREADS = 4;
    // Thread pool in which futures will be stored
    private static ExecutorService executorService;

    public static ExecutorService getExecutorService() {
        if(executorService == null) {
            executorService = Executors.newFixedThreadPool(MAX_THREADS);
        }

        return executorService;
    }

}
