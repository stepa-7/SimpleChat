package com.example.mysimplechat.chat.chatroom;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatRoom> getRoomList () {
        return chatRoomRepository.findAll();
    }

    public void deleteRoomByReceiverId (String receiverId, String senderId) {
        chatRoomRepository.removeChatRoomByReceiverIdAndSenderId(receiverId, senderId);
    }
}
