package com.stepa7.webchat.websockets.client;

import com.stepa7.webchat.model.entity.Message;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.util.concurrent.ExecutionException;

public class ChatStompClient {
    private StompSession session;
    private final String username;
    private final String url;
    private final WebSocketStompClient stompClient;
    private final ChatStompSessionHandler sessionHandler;

    public ChatStompClient(String username,
                           String serverUrl,
                           WebSocketStompClient stompClient,
                           ChatStompSessionHandler sessionHandler) {
        this.username = username;
        this.stompClient = stompClient;
        this.sessionHandler = sessionHandler;
        this.url = serverUrl + "?username=" + username;
//        url = "ws://localhost:8080/chat?username=" + username;
    }

    public void disconnect() {
        if ((session == null) || !session.isConnected()) {
            System.out.println("Not connected to server");
            return;
        }
        session.disconnect();
        System.out.println("Disconnected from server");
    }

    public void connect() throws ExecutionException, InterruptedException {
        if ((session != null) && session.isConnected()) {
            System.out.println("Already connected to server");
            return;
        }

        StompHeaders headers = new StompHeaders();
        headers.add("username", username); // Передаем username в заголовках

        session = stompClient.connectAsync(url, sessionHandler, headers).get();
        System.out.println("Connected to server");
    }

    public void sendMessage(Message message) {
        session.send("/app/broadcast-message", message); // Connected to controller
        System.out.println("Message sent: " + message.getMessage());
    }

    public void sendPrivateMessage(Message message) {
        session.send("/app/private-message", message); // Connected to controller
        System.out.println("Message sent: " + message.getMessage());
    }
}