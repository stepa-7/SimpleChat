package com.stepa7.webchat.websockets.client;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatStompClient {
    private StompSession session;
    private final String username;
    private final ChatController chatController;

    private final WebSocketStompClient stompClient;
    private final String url;
    private final ChatStompSessionHandler sessionHandler;
    private final StompHeaders headers;

    public ChatStompClient(String username, ChatController chatController) throws ExecutionException, InterruptedException {
        this.username = username;
        this.chatController = chatController;

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient sockJsClient = new SockJsClient(transports);
        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter()); // To serialize and deserialize messages into json
        sessionHandler = new ChatStompSessionHandler(username, chatController);
        url = "ws://localhost:8080/chat?username=" + username;

        headers = new StompHeaders();
        headers.add("username", username); // Передаем username в заголовках

        this.connect();
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
        session = stompClient.connectAsync(url, sessionHandler, headers).get();
        System.out.println("Connected to server");
    }

    public void sendMessage(ChatMessage message) {
        session.send("/app/broadcast-message", message); // Connected to controller
        System.out.println("Message sent: " + message.getMessage());
    }

    public void sendPrivateMessage(ChatMessage message) {
        session.send("/app/private-message", message); // Connected to controller
        System.out.println("Message sent: " + message.getMessage());
    }
}