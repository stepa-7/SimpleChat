package com.example.mysimplechat.chat.chatroom;

import com.example.mysimplechat.chat.chatmessage.ChatMessage;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
    private String chatId;
    private String senderId;
    private String receiverId;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatMessage> messages;

    public ChatRoom() {}

    public ChatRoom(String chatId, String senderId, String receiverId, List<ChatMessage> messages) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messages = messages;
    }

    public ChatRoom(String chatId, String senderId, String receiverId) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
