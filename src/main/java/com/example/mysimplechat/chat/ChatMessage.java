package com.example.mysimplechat.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage {
    private String sender;
    private String message;
    private String receiver;

    @JsonCreator
    public ChatMessage(
            @JsonProperty("sender") String sender,
            @JsonProperty("message") String message,
            @JsonProperty("receiver") String receiver) {
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
    }

    public ChatMessage() {}

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }
}
