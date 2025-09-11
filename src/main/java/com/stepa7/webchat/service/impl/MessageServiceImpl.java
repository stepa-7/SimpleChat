package com.stepa7.webchat.service.impl;

import com.stepa7.webchat.model.entity.Message;
import com.stepa7.webchat.repository.MessageRepository;
import com.stepa7.webchat.service.MessageService;
import com.stepa7.webchat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository repository;
    private final RoomService roomService;

    @Override
    public Message save(Message chatMessage) {
        String chatRoomId = roomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true).orElse("");
        chatMessage.setChatId(chatRoomId);
        return repository.save(chatMessage);
    }
}
