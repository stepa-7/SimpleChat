package com.example.mysimplechat.chat.websockets;

import com.example.mysimplechat.chat.ChatMessage;
import com.example.mysimplechat.chat.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChatWebsocketController {
    private final SimpMessagingTemplate messagingTemplate; // Websocket use this to send messages to users
    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatWebsocketController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

//    @GetMapping("/messages/{senderId}/{receiverId}")
//    public ResponseEntity<List<ChatMessage>> findChatMessages(
//            @PathVariable String senderId,
//            @PathVariable String receiverId
//    ) {
//        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, receiverId));
//    }

    // User sends a message to the server to /app/private-message
    @MessageMapping("/private-message")
    public void handlePrivateMessage(@Payload ChatMessage message) {
        System.out.println("Received message from user: " + message.getSenderId() + ": " + message.getMessage());
        chatMessageService.save(message);
        // mary/queue/messages
        messagingTemplate.convertAndSendToUser(message.getReceiverId(), "/queue/messages", message);
        System.out.println("Sent message to /queue/messages: " + message.getSenderId() + ": " + message.getMessage());
    }

    // User sends a message to the server to /app/broadcast-message
    @MessageMapping("/broadcast-message")
    public void handleBroadcastMessage(@Payload ChatMessage message) {
        System.out.println("Received message from user: " + message.getSenderId() + ": " + message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages", message); // This route is used by websocket to broadcast a user's message to other users
        System.out.println("Sent message to /topic/messages: " + message.getSenderId() + ": " + message.getMessage());
    }
}
