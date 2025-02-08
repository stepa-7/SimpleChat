package com.example.mysimplechat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private Label loginLabel;
    @FXML
    private Button registerButton;

    @FXML
    public void onLoginClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onRegisterClick(MouseEvent mouseEvent) {

    }
}
