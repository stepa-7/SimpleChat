package com.example.mysimplechat.chat.websockets.client;

import com.example.mysimplechat.chat.ChatController;
import com.example.mysimplechat.chat.ChatMessage;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatStompClient {
    private final StompSession session;
    private final String username;
    private final ChatController chatController;

    public ChatStompClient(String username, ChatController chatController) throws ExecutionException, InterruptedException {
        this.username = username;
        this.chatController = chatController;

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter()); // To serialize and deserialize messages into json
        ChatStompSessionHandler sessionHandler = new ChatStompSessionHandler(username, chatController);
        String url = "ws://localhost:8080/chat?username=" + username;

        StompHeaders headers = new StompHeaders();
        headers.add("username", username); // Передаем username в заголовках

        session = stompClient.connectAsync(url, sessionHandler, headers).get();
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