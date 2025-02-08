package com.example.validation;

import javafx.scene.control.Alert;

import java.util.regex.Pattern;

public class UserValidator {
    public static boolean checkCorrectness (String login, String password) {
        boolean isFail = false;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        isFail = isFail(login, password, alert);

        if (isFail) {
            alert.setHeaderText(null);
            alert.show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean isFail(String login, String password, Alert alert) {
        if(!Pattern.matches("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{3,30}$", login)) {
            alert.setTitle("Wrong login");
            alert.setContentText("Login should be at least 4 characters long and contain only letters and numbers");
            return true;
        } else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,}$", password)) {
            alert.setTitle("Wrong password");
            alert.setContentText("Password should be at least 5 characters long and contain at least one digit");
            return true;
        }
        return false;
    }

    public static boolean checkCorrectness(String username, String login, String password) {
        boolean isFail = false;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if(!Pattern.matches("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{3,30}$", username)) {
            alert.setTitle("Wrong username");
            alert.setContentText("Username should be at least 4 characters long and contain only letters and numbers");
            isFail = true;
        } else isFail = isFail(login, password, alert);

        if (isFail) {
            alert.setHeaderText(null);
            alert.show();
            return false;
        } else {
            return true;
        }
    }
}
