//package com.example.mysimplechat.chat.chatroom;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class ChatRoomController {
//    private final ChatRoomService chatRoomService;
//
//    @Autowired
//    public ChatRoomController(ChatRoomService chatRoomService) {
//        this.chatRoomService = chatRoomService;
//    }
//
//
//    @GetMapping("/chat-rooms")
//    public List<ChatRoom> getChatRooms() {
//        return chatRoomService.getRoomList();
//    }
//
//    @PostMapping("/delete-room")
//    public ResponseEntity<String> deleteRoom (@RequestParam String receiverId, @RequestParam String senderId) {
//        chatRoomService.deleteRoomByReceiverId(receiverId, senderId);
//        return ResponseEntity.ok("Chat deleted successfully");
//    }
//}
