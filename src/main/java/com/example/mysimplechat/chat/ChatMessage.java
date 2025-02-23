package com.example.mysimplechat.chat;

import com.example.mysimplechat.chat.chatroom.ChatRoom;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "messages")
public class ChatMessage {
    private String chatId;
    private String senderId;
    private String message;
    private String receiverId;
    private Date timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
//    @JoinColumn(name = "chatId")
    private ChatRoom chatRoom;

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    @JsonCreator
    public ChatMessage(
            @JsonProperty("senderId") String senderId,
            @JsonProperty("message") String message,
            @JsonProperty("receiverId") String receiverId) {
        this.senderId = senderId;
        this.message = message;
        this.receiverId = receiverId;
    }

    public ChatMessage() {}

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
