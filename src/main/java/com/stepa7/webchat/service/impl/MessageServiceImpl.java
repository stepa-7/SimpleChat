package com.stepa7.webchat.service.impl;

import com.stepa7.webchat.model.entity.Message;
import com.stepa7.webchat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl {
    private final MessageRepository repository;
    private final ChatService chatService;

    public Message save(Message chatMessage) {
        String chatRoomId = chatService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true).orElse("");
        chatMessage.setChatId(chatRoomId);
        return repository.save(chatMessage);
    }
}
