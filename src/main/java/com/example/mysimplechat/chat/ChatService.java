package com.example.mysimplechat.chat;

import com.example.mysimplechat.chat.chatmessage.ChatMessage;
import com.example.mysimplechat.chat.chatmessage.ChatMessageRepository;
import com.example.mysimplechat.chat.chatroom.ChatRoom;
import com.example.mysimplechat.chat.chatroom.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }


    public Optional<String> getChatRoomId (
            String senderId,
            String receiverId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatId)
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

    private String createChatId(String senderId, String receiverId) {
        String chatId = String.format("%s_%s", senderId, receiverId);
        ChatRoom senderReceiverChatRoom = new ChatRoom(chatId, senderId, receiverId);
        ChatRoom recieverSenderChatRoom = new ChatRoom(chatId, receiverId, senderId);
        chatRoomRepository.save(senderReceiverChatRoom);
        chatRoomRepository.save(recieverSenderChatRoom);
        return chatId;
    }

    public List<ChatMessage> findChatMessages(String senderId, String receiverId, boolean createNewRoomIfNotExists) {
        if (createNewRoomIfNotExists) {
            var chatId = this.getChatRoomId(senderId, receiverId, true);
            return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
        } else {
            String chatId = String.format("%s_%s", senderId, receiverId);
            return chatMessageRepository.findByChatId(chatId);
        }
    }
}
