package com.stepa7.webchat.model.entity;

//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "messages")
public class Message {
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
    private Room room;

//    @JsonCreator
//    public Message(
//            @JsonProperty("senderId") String senderId,
//            @JsonProperty("message") String message,
//            @JsonProperty("receiverId") String receiverId,
//            @JsonProperty("timestamp") Date timestamp) {
//        this.senderId = senderId;
//        this.message = message;
//        this.receiverId = receiverId;
//        this.timestamp = timestamp;
//    }
}