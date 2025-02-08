package com.example.mysimplechat;

import com.example.database.DatabaseUtil;
import com.example.security.SecurityUtil;
import com.example.validation.UserValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
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
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onLoginClick(MouseEvent mouseEvent) throws Exception {
        String login = loginLogin.getText();
        String password = passwordLogin.getText();
        if (!UserValidator.checkCorrectness(login, password)) { return; }
        if (SecurityUtil.checkPassword(password, DatabaseUtil.getPasswordHash(login))) {
            FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("chat-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) registerLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
}