package com.example.mysimplechat.authorization;

import com.example.database.DatabaseUtil;
import com.example.mysimplechat.chat.SimpleChat;
import com.example.security.SecurityUtil;
import com.example.validation.UserValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;

public class RegisterController {
    @FXML
    private Label loginLabel;
    @FXML
    private TextField usernameRegister;
    @FXML
    private TextField loginRegister;
    @FXML
    private TextField passwordRegister;

    public TextField getLoginRegister() {
        return loginRegister;
    }
    public TextField getPasswordRegister() {
        return passwordRegister;
    }

    @FXML
    public void onLoginClick(MouseEvent mouseEvent) throws IOException {
        switchSceneOnLogin(loginRegister.getText(), passwordRegister.getText());
    }

    @FXML
    public void onRegisterClick(MouseEvent mouseEvent) throws Exception {
        String username = usernameRegister.getText();
        String login = loginRegister.getText();
        String password = passwordRegister.getText();
        if (!UserValidator.checkCorrectness(username, login, password)) { return; }
        if (DatabaseUtil.isUsernameExists(username)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Username already exists!");
            alert.show();
            return;
        }
        if (DatabaseUtil.isLoginExists(login)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login already exists!");
            alert.show();
            return;
        }
        if (DatabaseUtil.saveUser(username, login, SecurityUtil.hashPassword(password)) != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Registration is completed!");
            alert.setContentText("Please log in with the same data.");
            alert.setOnHidden(event -> {
                try {
                    switchSceneOnLogin(login, password);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            alert.show();

        }
    }

    public void switchSceneOnLogin(String login, String password) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginLabel.getScene().getWindow();
        LoginController loginController = fxmlLoader.getController();
        stage.setScene(scene);
//        if (isRegistered) {
        loginController.getLoginLogin().setText(login);
        loginController.getPasswordLogin().setText(password);
//        }
        stage.show();
    }
}
