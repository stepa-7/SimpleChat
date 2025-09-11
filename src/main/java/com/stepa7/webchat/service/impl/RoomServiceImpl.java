package com.stepa7.webchat.service.impl;

import com.stepa7.webchat.model.entity.Message;
import com.stepa7.webchat.model.entity.Room;
import com.stepa7.webchat.repository.MessageRepository;
import com.stepa7.webchat.repository.RoomRepository;
import com.stepa7.webchat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<Room> getRoomList() {
        return roomRepository.findAll();
    }

    @Override
    public void deleteRoomByReceiverId(String receiverId, String senderId) {
        roomRepository.removeChatRoomByReceiverIdAndSenderId(receiverId, senderId);
    }

    @Override
    public Optional<String> getChatRoomId(
            String senderId,
            String receiverId,
            boolean createNewRoomIfNotExists
    ) {
        return roomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(Room::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        if (this.findChatMessages(receiverId, senderId, false).isEmpty()) {
                            String chatId = createChatId(senderId, receiverId);
                            return Optional.of(chatId);
                        } else {
                            String chatId = createChatId(receiverId, senderId);
                            return Optional.of(chatId);
                        }
                    }
                    return Optional.empty();
                });
    }

    @Override
    public List<Message> findChatMessages(String senderId, String receiverId, boolean createNewRoomIfNotExists) {
        if (createNewRoomIfNotExists) {
            var chatId = this.getChatRoomId(senderId, receiverId, true);
            return chatId.map(messageRepository::findByChatId).orElse(new ArrayList<>());
        } else {
            String chatId = String.format("%s_%s", senderId, receiverId);
            return messageRepository.findByChatId(chatId);
        }
    }

    @Override
    public String createChatId(String senderId, String receiverId) {
        String chatId = String.format("%s_%s", senderId, receiverId);
        Room senderReceiverChatRoom = new Room(chatId, senderId, receiverId);
        Room recieverSenderChatRoom = new Room(chatId, receiverId, senderId);
        roomRepository.save(senderReceiverChatRoom);
        roomRepository.save(recieverSenderChatRoom);
        return chatId;
    }
}
