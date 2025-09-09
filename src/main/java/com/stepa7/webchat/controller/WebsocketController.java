package com.stepa7.webchat.controller;

import com.stepa7.webchat.model.entity.Message;
import com.stepa7.webchat.model.entity.Room;
import com.stepa7.webchat.service.impl.ChatService;
import com.stepa7.webchat.service.impl.MessageServiceImpl;
import com.stepa7.webchat.service.impl.RoomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebsocketController {
    private final SimpMessagingTemplate messagingTemplate; // Websocket use this to send messages to users
    private final MessageServiceImpl messageService;
    private final RoomServiceImpl roomService;
    private final ChatService chatService;

    @GetMapping("/chat-rooms")
    @ResponseBody
    public List<Room> getChatRooms() {
        return roomService.getRoomList();
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String receiverId
    ) {
        return ResponseEntity.ok(chatService.findChatMessages(senderId, receiverId, true));
    }

    @PostMapping("/delete-room")
    @ResponseBody
    public ResponseEntity<String> deleteRoom (@RequestParam String receiverId, @RequestParam String senderId) {
        roomService.deleteRoomByReceiverId(receiverId, senderId);
        return ResponseEntity.ok("Chat deleted successfully");
    }

    // User sends a message to the server to /app/private-message
    @MessageMapping("/private-message")
    public void handlePrivateMessage(@Payload Message message) {
        System.out.println("Received message from user: " + message.getSenderId() + ": " + message.getMessage());
        messageService.save(message);
        // mary/queue/messages
        messagingTemplate.convertAndSendToUser(message.getReceiverId(), "/queue/messages", message);
        System.out.println("Sent message to /queue/messages: " + message.getSenderId() + ": " + message.getMessage());
    }

    // User sends a message to the server to /app/broadcast-message
    @MessageMapping("/broadcast-message")
    public void handleBroadcastMessage(@Payload Message message) {
        System.out.println("Received message from user: " + message.getSenderId() + ": " + message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages", message); // This route is used by websocket to broadcast a user's message to other users
        System.out.println("Sent message to /topic/messages: " + message.getSenderId() + ": " + message.getMessage());
    }
}
