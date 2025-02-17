package com.example.mysimplechat.chat;

import com.example.database.DatabaseUtil;
import com.example.mysimplechat.chat.client.ChatStompClient;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

    private ChatStompClient chatClient = null;

    public ChatController() {}

    public Label getUsernameLabel() {
        return usernameLabel;
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
    }

    public void onChatClick(MouseEvent mouseEvent) throws ExecutionException, InterruptedException {
//        chatClient = new ChatStompClient(usernameLabel.getText());
        chatterUsernameLabel.setText(usersListView.getSelectionModel().getSelectedItem().toString()); // need to handle nullpointer

    }

    public void onSendClick(MouseEvent mouseEvent) throws ExecutionException, InterruptedException {
//        chatClient = new ChatStompClient(chatterUsernameLabel.getText());
        String message = typedMessageTextField.getText();
        chatClient.sendMessage(new ChatMessage(usernameLabel.getText(), message, chatterUsernameLabel.getText())); // IllegalStateException connection closed
        typedMessageTextField.clear();
    }

    public void connectToServer(String username) throws ExecutionException, InterruptedException {
       chatClient = new ChatStompClient(username, this);
    }

    public void updateMessagesTextArea (ChatMessage message) {
        messagesTextArea.appendText(message.getSender() + '\n' + message.getMessage() + '\n' + '\n');
    }
}