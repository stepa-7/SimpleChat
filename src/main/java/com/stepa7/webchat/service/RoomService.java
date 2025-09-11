package com.stepa7.webchat.service;

import com.stepa7.webchat.model.entity.Message;
import com.stepa7.webchat.model.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<Room> getRoomList();

    void deleteRoomByReceiverId(String receiverId, String senderId);

    Optional<String> getChatRoomId(
            String senderId,
            String receiverId,
            boolean createNewRoomIfNotExists
    );

    List<Message> findChatMessages(String senderId, String receiverId, boolean createNewRoomIfNotExists);

    String createChatId(String senderId, String receiverId);
}
