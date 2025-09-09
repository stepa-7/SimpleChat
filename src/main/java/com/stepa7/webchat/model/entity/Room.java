package com.stepa7.webchat.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_rooms")
public class Room {
    private String chatId;
    private String senderId;
    private String receiverId;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    public Room(String chatId, String senderId, String receiverId) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
