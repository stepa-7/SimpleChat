package com.stepa7.webchat.service.impl;

import com.stepa7.webchat.model.entity.Room;
import com.stepa7.webchat.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl {
    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getRoomList () {
        return roomRepository.findAll();
    }

    public void deleteRoomByReceiverId (String receiverId, String senderId) {
        roomRepository.removeChatRoomByReceiverIdAndSenderId(receiverId, senderId);
    }
}
