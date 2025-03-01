package com.example.mysimplechat.chat.chatroom;

import com.example.mysimplechat.chat.chatmessage.ChatMessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
//    private final ChatMessageService chatMessageService;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

//    public Optional<String> getChatRoomId (
//            String senderId,
//            String receiverId,
//            boolean createNewRoomIfNotExists
//    ) {
//        return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
//                .map(ChatRoom::getChatId)
//                .or(() -> {
//                    if (createNewRoomIfNotExists) {
////                        if (chatMessageService.findChatMessages(receiverId, senderId).isEmpty()) {
////                            String chatId = createChatId(senderId, receiverId);
////                            return Optional.of(chatId);
////                        } else {
//                            String chatId = createChatId(receiverId, senderId);
//                            return Optional.of(chatId);
////                        }
//                    }
//                    return Optional.empty();
//                });
//    }

//    private String createChatId(String senderId, String receiverId) {
//        String chatId = String.format("%s_%s", senderId, receiverId);
//        ChatRoom senderReceiverChatRoom = new ChatRoom(chatId, senderId, receiverId);
//        ChatRoom recieverSenderChatRoom = new ChatRoom(chatId, receiverId, senderId);
//        chatRoomRepository.save(senderReceiverChatRoom);
//        chatRoomRepository.save(recieverSenderChatRoom);
//        return chatId;
//    }

    public List<ChatRoom> getRoomList () {
        return chatRoomRepository.findAll();
    }

    public void deleteRoomByReceiverId (String receiverId, String senderId) {
        chatRoomRepository.removeChatRoomByReceiverIdAndSenderId(receiverId, senderId);
    }
}
