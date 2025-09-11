package com.stepa7.webchat.service.impl;

import com.stepa7.webchat.service.ChatService;
import com.stepa7.webchat.websockets.client.ChatStompClient;
import com.stepa7.webchat.websockets.client.ChatStompSessionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final WebSocketStompClient stompClient;

    @Value("${websocket.server.url}")
    private String websockerServerUrl;

    private ChatStompClient chatClient = null;

    @Override
    public void connectToServer(String username) throws ExecutionException, InterruptedException {
        if (chatClient != null) {
            chatClient.connect();
        } else {
            ChatStompSessionHandler sessionHandler = new ChatStompSessionHandler(username);
            chatClient = new ChatStompClient(username, websockerServerUrl, stompClient, sessionHandler);
            chatClient.connect();
        }
    }

    @Override
    public void disconnectFromServer() {
        chatClient.disconnect();
    }
}
