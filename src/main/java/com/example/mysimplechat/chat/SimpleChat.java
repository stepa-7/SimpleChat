package com.example.mysimplechat.chat;

import com.example.mysimplechat.chat.chatroom.ChatRoom;
import com.example.mysimplechat.chat.chatroom.ChatRoomService;
import jakarta.annotation.PostConstruct;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SimpleChat extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        launch(args);

//        ChatStompClient chatStompClient = new ChatStompClient("stepa177");
//        chatStompClient.sendMessage(new ChatMessage("stepa177", "Hello world", ""));
    }
}