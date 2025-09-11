package com.stepa7.webchat.service;

import java.util.concurrent.ExecutionException;

public interface ChatService {
    void connectToServer(String username) throws ExecutionException, InterruptedException;

    void disconnectFromServer();
}
