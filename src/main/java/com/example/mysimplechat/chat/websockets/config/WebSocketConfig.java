package com.example.mysimplechat.chat.websockets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration(proxyBeanMethods = false)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .addInterceptors(new ChatHandshakeInterceptor())
                .setHandshakeHandler(new ChatHandshakeHandler())
                .withSockJS(); // user connects to /chat and to WebSocket server
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // user subscribes to /topic and /queue to receive messages from the server
        registry.setApplicationDestinationPrefixes("/app"); // user sends messages to /app
        registry.setUserDestinationPrefix("/user");
//        registry.setUserDestinationPrefix("/user");
    }
}
