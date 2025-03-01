package com.example.database;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConnectionChecker {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static String login;

    public static void startConnectionChecker(String login) {
        ConnectionChecker.login = login;
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (DatabaseUtil.isLoginExists(login)) {
                    DatabaseUtil.sendHeartbeat(login);
                } else {
                    scheduler.shutdown();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}
