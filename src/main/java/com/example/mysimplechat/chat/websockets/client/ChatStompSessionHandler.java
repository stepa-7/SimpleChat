package com.example.mysimplechat.chat.websockets.client;

import com.example.mysimplechat.chat.ChatController;
import com.example.mysimplechat.chat.chatmessage.ChatMessage;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class ChatStompSessionHandler extends StompSessionHandlerAdapter {
    private final String username;
    private final ChatController chatController;


    public ChatStompSessionHandler(String username, ChatController chatController) {
        this.username = username;
        this.chatController = chatController;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Client connected");
        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class; // Convert incoming json data into a message object
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload instanceof ChatMessage) {
                    ChatMessage message = (ChatMessage) payload;
                    chatController.updateMessagesTextArea(message);
                    System.out.println("Received message: " + message.getSenderId() + ": " + message.getMessage());
                } else {
                    System.out.println("Received unexpected message type: " + payload.getClass());
                }
            }
        });
        session.subscribe("/user/queue/messages", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return ChatMessage.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        if (payload instanceof ChatMessage) {
                            ChatMessage message = (ChatMessage) payload;
                            chatController.updateMessagesTextArea(message);
                            System.out.println("Received message: " + message.getSenderId() + ": " + message.getMessage());
                        } else {
                            System.out.println("Received unexpected message type: " + payload.getClass());
                        }
                    }
                });
        System.out.println("Client subscribed to /topic/messages");
        System.out.println("Client subscribed to /user/queue/messages");
//        System.out.println("Client subscribed to /user/queue/messages");
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        super.handleTransportError(session, exception);
    }
}
