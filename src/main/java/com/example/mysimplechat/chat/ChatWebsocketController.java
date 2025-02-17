package com.example.mysimplechat.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebsocketController {
    private SimpMessagingTemplate messagingTemplate; // Websocket use this to send messages to users

    @Autowired
    public ChatWebsocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // User sends a message to the server to /app/private-message
    @MessageMapping("/private-message")
    public void handlePrivateMessage(ChatMessage message) {
        System.out.println("Received message from user: " + message.getSender() + ": " + message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages", message); // This route is used by websocket to broadcast a user's message to other users
        System.out.println("Sent message to /topic/messages: " + message.getSender() + ": " + message.getMessage());
    }
}
