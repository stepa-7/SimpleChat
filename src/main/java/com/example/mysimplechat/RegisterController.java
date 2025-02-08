package com.example.mysimplechat;

import com.example.database.DatabaseUtil;
import com.example.security.SecurityUtil;
import com.example.validation.UserValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.regex.Pattern;


import java.io.IOException;

public class RegisterController {
    @FXML
    private Label loginLabel;
    @FXML
    private Button registerButton;
    @FXML
    private TextField usernameRegister;
    @FXML
    private TextField loginRegister;
    @FXML
    private TextField passwordRegister;

    private boolean isRegistered = false;

    @FXML
    public void onLoginClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onRegisterClick(MouseEvent mouseEvent) throws Exception {
        String username = usernameRegister.getText();
        String login = loginRegister.getText();
        String password = passwordRegister.getText();
        if (!UserValidator.checkCorrectness(username, login, password)) { return; }
        if (DatabaseUtil.saveUser(username, login, SecurityUtil.hashPassword(password)) != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Registration is completed!");
            alert.setContentText("Please log in with the same data.");
            isRegistered = true;
            alert.setOnHidden(event -> {
                try {
                    switchScene(login, password);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            alert.show();

        }
    }

    private void switchScene(String login, String password) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginLabel.getScene().getWindow();
        LoginController loginController = fxmlLoader.getController();
        stage.setScene(scene);
        if (isRegistered) {
            loginController.getLoginLogin().setText(login);
            loginController.getPasswordLogin().setText(password);
        }
        stage.show();
    }
}
