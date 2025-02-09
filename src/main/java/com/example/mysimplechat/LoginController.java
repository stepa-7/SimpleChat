package com.example.mysimplechat;

import com.example.database.DatabaseUtil;
import com.example.security.SecurityUtil;
import com.example.validation.UserValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
                FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("chat-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) registerLabel.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
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
}