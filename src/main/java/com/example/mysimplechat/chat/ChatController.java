package com.example.mysimplechat.chat;

import com.example.database.DatabaseUtil;
import com.example.mysimplechat.authorization.LoginController;
import com.example.mysimplechat.authorization.RegisterController;
import com.example.mysimplechat.authorization.RoomsCallback;
import com.example.mysimplechat.chat.chatmessage.ChatMessage;
import com.example.mysimplechat.chat.chatmessage.MessagesCallback;
import com.example.mysimplechat.chat.chatroom.ChatRoom;
import com.example.mysimplechat.chat.websockets.client.ChatStompClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ChatController {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label chatterUsernameLabel;
    @FXML
    private ListView usersListView;
    @FXML
    private TextArea messagesTextArea;
    @FXML
    private TextField typedMessageTextField;
    @FXML
    private ScrollPane messagesScrollPane;


    MessagesCallback messagesCallback = new MessagesCallback() {
        @Override
        public void onSuccess(List<ChatMessage> messages) {
            Platform.runLater(() -> {
                for (ChatMessage message : messages) {
                    updateMessagesTextArea(message);
                }
                new Thread(() -> {
                    try {
                        Thread.sleep(100); // Задержка 100 мс
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    Platform.runLater(() -> messagesTextArea.setScrollTop(Double.MAX_VALUE));
                }).start();
            });
        }

        @Override
        public void onError(IOException e) {
            System.out.println(e.getMessage());
        }
    };

    private ChatStompClient chatClient = null;

    public ChatController() {}

    public Label getUsernameLabel() {
        return usernameLabel;
    }

    public ListView getUsersListView() {
        return usersListView;
    }

    public Label getChatterUsernameLabel() {
        return chatterUsernameLabel;
    }

    public void onExitClick(MouseEvent mouseEvent) throws Exception {
        String myUsername = usernameLabel.getText();
        DatabaseUtil.setIsConnected(DatabaseUtil.getLogin(myUsername), false);
        disconnectFromServer(myUsername);
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        RegisterController registerController = fxmlLoader.getController();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        switchSceneOnLogin(stage, "", "");
    }

    public void switchSceneOnLogin(Stage stage, String login, String password) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        LoginController loginController = fxmlLoader.getController();

        loginController.getLoginLogin().setText(login);
        loginController.getPasswordLogin().setText(password);

        stage.setScene(scene);
        stage.show();
    }

    public void onAddChatClick(MouseEvent mouseEvent) throws Exception {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Enter username");

        Label label = new Label("Username: ");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField textField = new TextField();

        GridPane gridPane = new GridPane();
        gridPane.add(label, 0, 0);
        gridPane.add(textField, 1, 0);
        gridPane.setMinSize(300, 100);
        gridPane.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
             if (button == ButtonType.OK) {
                 return textField.getText();
             }
             return null;
        });

        dialog.showAndWait().ifPresent(username -> {
            try {
                if (DatabaseUtil.isUsernameExists(username)) {
                    if (usernameLabel.getText().equals(username)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("You can't chat with yourself!");
                        alert.show();
                        return;
                    } else {
                        for (int i = 0; i < usersListView.getItems().size(); i++) {
                            if (usersListView.getItems().get(i).equals(username)) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("You have already added this user!");
                                alert.show();
                                return;
                            }
                        }
                        usersListView.getItems().add(username);
                        chatterUsernameLabel.setText(username);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("User not found!");
                    alert.show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        usersListView.getSelectionModel().select(chatterUsernameLabel.getText());
        messagesTextArea.clear();
        loadMessages(messagesCallback, usernameLabel.getText(), chatterUsernameLabel.getText());
    }

    public void onChatClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            String selectedChat = usersListView.getSelectionModel().getSelectedItem().toString();
            String myUsername = usernameLabel.getText();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting chat");
            alert.setHeaderText("Do you want to delete this chat?");
            alert.setContentText("The chat: " + selectedChat + " will be deleted");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                usersListView.getItems().remove(selectedChat);

                RequestBody formBody = new FormBody.Builder()
                        .add("receiverId", myUsername)
                        .add("senderId", selectedChat)
                        .build();

                Request request = new Request.Builder()
                        .url("http://localhost:8080/delete-room")
                        .post(formBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                Call call = client.newCall(request);
                Response response = call.execute();
                if (response.code() == 200) {
                    System.out.println("The room was successfully deleted");
                }
                response.close();
            } else {
                return;
            }
        }
        String chatter = usersListView.getSelectionModel().getSelectedItem().toString();
        chatterUsernameLabel.setText(chatter); // need to handle nullpointer
        messagesTextArea.clear();
        loadMessages(messagesCallback, usernameLabel.getText(), chatter);
    }

    private void loadMessages(MessagesCallback callback, String sender, String receiver) {
        Request request = new Request.Builder()
                .url("http://localhost:8080/messages/" + sender + "/" + receiver)
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonString = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                System.out.println(jsonString);
                List<ChatMessage> messages = mapper
                        .readValue(jsonString, new TypeReference<List<ChatMessage>>() {});
                callback.onSuccess(messages);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(e);
//                call.cancel();
            }
        });
//        if (call.isCanceled()) {
//
//        }
    }

    public void onSendClick(MouseEvent mouseEvent) {
//        chatClient = new ChatStompClient(chatterUsernameLabel.getText());
        String message = typedMessageTextField.getText();
        if (message.isEmpty()) {
            return;
        }

        String chatterId = chatterUsernameLabel.getText();

        ChatMessage chatMessage = new ChatMessage(usernameLabel.getText(), message, chatterId, Date.from(Instant.now()));
        if (chatterId.equals("General chat (With ALL users)")) {
            chatClient.sendMessage(chatMessage); // IllegalStateException connection closed
        } else {
            chatClient.sendPrivateMessage(chatMessage);
            updateMessagesTextArea(chatMessage);
        }
        typedMessageTextField.clear();
    }

    public void connectToServer(String username) throws ExecutionException, InterruptedException {
        if (chatClient != null) {
            chatClient.connect();
        } else {
            chatClient = new ChatStompClient(username, this);
        }
    }

    public void disconnectFromServer(String username) {
        chatClient.disconnect();
    }

    public void updateMessagesTextArea (ChatMessage message) {
        String chatterId = chatterUsernameLabel.getText();
        String myId = usernameLabel.getText();
        if (message.getSenderId().equals(chatterId)
//                || (message.getReceiverId().equals(chatterId) && message.getSenderId().equals(myId))
                || (message.getReceiverId().equals(chatterId))) {
//            double scrollPosition = messagesScrollPane.getVvalue();
            Date date = message.getTimestamp();
            if (date != null) {
                messagesTextArea.appendText(date.toString() + '\n');
            }
            messagesTextArea.appendText(message.getSenderId() + '\n' + message.getMessage() + '\n' + '\n');

//            new Thread(() -> {
//                try {
//                    Thread.sleep(100); // Задержка 100 мс
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Platform.runLater(() -> messagesTextArea.setScrollTop(scrollPosition));
////                Platform.runLater(() -> messagesScrollPane.setVvalue(scrollPosition));
//            }).start();
        }

        if (!myId.equals(message.getSenderId()) && !usersListView.getItems().contains(message.getSenderId())) {
            updateUsersListView(myId);
        }
    }

    public void updateUsersListView(String myUsername) {
        getRooms(new RoomsCallback() {
            @Override
            public void onSuccess(List<ChatRoom> chatRooms) {
                Platform.runLater(() -> {
                    for (ChatRoom room : chatRooms) {
                        String sender = room.getSenderId();
                        String receiver = room.getReceiverId();
//                        if (sender.equals(myUsername) && !(receiver.equals(myUsername))) {
//                            if (!usersListView.getItems().contains(receiver)) {
//                                usersListView.getItems().add(receiver);
//                            }
//                        } else if (receiver.equals(myUsername) && !(sender.equals(myUsername))) {
                        if (receiver.equals(myUsername) && !(sender.equals(myUsername))) {
                            if (!usersListView.getItems().contains(sender)) {
                                usersListView.getItems().add(sender);
                            }
                        }
                    }
                });
            }


            @Override
            public void onError(IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void getRooms(RoomsCallback callback) {
        Request request = new Request.Builder()
                .url("http://localhost:8080/chat-rooms")
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonString = response.body().string();
                ObjectMapper mapper = new ObjectMapper();

                List<ChatRoom> chatRooms = mapper
                        .readValue(jsonString, new TypeReference<List<ChatRoom>>() {});
                callback.onSuccess(chatRooms);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(e);
            }
        });
    }

    public void onUsernameClick(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Username");
        alert.setContentText(usernameLabel.getText());
        alert.setHeaderText("Your username:");
        alert.show();
    }
}