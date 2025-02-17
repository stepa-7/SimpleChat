package com.example.mysimplechat.chat;

import com.example.mysimplechat.chat.client.ChatStompClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
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

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        launch();

//        ChatStompClient chatStompClient = new ChatStompClient("stepa177");
//        chatStompClient.sendMessage(new ChatMessage("stepa177", "Hello world", ""));
    }
}