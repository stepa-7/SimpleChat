package com.example.mysimplechat.chat.websockets;

import com.example.mysimplechat.chat.ChatService;
import com.example.mysimplechat.chat.chatmessage.ChatMessage;
import com.example.mysimplechat.chat.chatmessage.ChatMessageService;
import com.example.mysimplechat.chat.chatroom.ChatRoom;
import com.example.mysimplechat.chat.chatroom.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChatWebsocketController {
    private final SimpMessagingTemplate messagingTemplate; // Websocket use this to send messages to users
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @Autowired
    public ChatWebsocketController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService, ChatRoomService chatRoomService, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.chatService = chatService;
    }

    @GetMapping("/chat-rooms")
    @ResponseBody
    public List<ChatRoom> getChatRooms() {
        return chatRoomService.getRoomList();
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String receiverId
    ) {
        return ResponseEntity.ok(chatService.findChatMessages(senderId, receiverId, true));
    }

    @PostMapping("/delete-room")
    @ResponseBody
    public ResponseEntity<String> deleteRoom (@RequestParam String receiverId, @RequestParam String senderId) {
        chatRoomService.deleteRoomByReceiverId(receiverId, senderId);
        return ResponseEntity.ok("Chat deleted successfully");
    }

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
