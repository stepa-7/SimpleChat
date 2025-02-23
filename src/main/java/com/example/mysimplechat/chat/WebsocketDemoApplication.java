package com.example.mysimplechat.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(proxyBeanMethods = false)
@EnableScheduling
@EntityScan({"com.example.mysimplechat.chat.chatroom", "com.example.mysimplechat.chat"})
@ComponentScan({"com.example.mysimplechat", "com.example.mysimplechat.chat.websockets.config"})
//@EnableJpaRepositories(basePackages = {"com.example.mysimplechat.chat.chatroom", "com.example.mysimplechat.chat"})
public class WebsocketDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebsocketDemoApplication.class, args);
    }
}
