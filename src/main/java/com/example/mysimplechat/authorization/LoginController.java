package com.example.mysimplechat.authorization;

import com.example.database.ConnectionChecker;
import com.example.database.DatabaseUtil;
import com.example.mysimplechat.chat.ChatController;
import com.example.mysimplechat.chat.SimpleChat;
import com.example.mysimplechat.chat.chatroom.ChatRoomService;
import com.example.security.SecurityUtil;
import com.example.validation.UserValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

//@Controller
public class LoginController {
//    private final ChatRoomService chatRoomService;

//    @Autowired
//    public LoginController(ChatRoomService chatRoomService) {
//        this.chatRoomService = chatRoomService;
//    }

    @FXML
    private Label registerLabel;
    @FXML
    private TextField loginLogin;
    @FXML
    private TextField passwordLogin;
    @FXML
    private Button loginButton;

    public TextField getPasswordLogin() {
        return passwordLogin;
    }
    public TextField getLoginLogin() {
        return loginLogin;
    }

    @FXML
    public void onRegisterClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) registerLabel.getScene().getWindow();
        RegisterController registerController = fxmlLoader.getController();
        stage.setScene(scene);
        registerController.getLoginRegister().setText(loginLogin.getText());
        registerController.getPasswordRegister().setText(passwordLogin.getText());
        stage.show();
    }

    @FXML
    public void onLoginClick(MouseEvent mouseEvent) throws Exception {
        String login = loginLogin.getText();
        String password = passwordLogin.getText();
        if (!UserValidator.checkCorrectness(login, password)) { return; }

        String hashedPassword = DatabaseUtil.getPasswordHash(login);
        if (hashedPassword != null) {
            if (SecurityUtil.checkPassword(password, hashedPassword)) {
                changeToChatView(login);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Login or password is incorrect!");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("User not found!");
            alert.show();
        }
    }

    public void changeToChatView(String login) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) registerLabel.getScene().getWindow();
        ChatController chatController = fxmlLoader.getController();
        chatController.getUsernameLabel().setText(DatabaseUtil.getUsername(login));
        chatController.getUsersListView().getItems().add("General chat (With ALL users)");
        chatController.getUsersListView().getSelectionModel().select("General chat (With ALL users)");
        chatController.getChatterUsernameLabel().setText("General chat (With ALL users)");

        DatabaseUtil.setIsConnected(login, true);
        ConnectionChecker.startConnectionChecker(login);

//        chatRoomService.getRoomList();

        stage.setScene(scene);
        stage.show();

        chatController.connectToServer(DatabaseUtil.getUsername(login));
    }
}