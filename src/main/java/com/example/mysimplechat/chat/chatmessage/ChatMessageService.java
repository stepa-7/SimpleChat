package com.example.mysimplechat.chat.chatmessage;

import com.example.mysimplechat.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatService chatService;

    @Autowired
    public ChatMessageService(ChatMessageRepository repository, ChatService chatService) {
        this.repository = repository;
        this.chatService = chatService;
    }

    public ChatMessage save(ChatMessage chatMessage) {
        String chatRoomId = chatService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true).orElse("");
        chatMessage.setChatId(chatRoomId);
        return repository.save(chatMessage);
    }
}
