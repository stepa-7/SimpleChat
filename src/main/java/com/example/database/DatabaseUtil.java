package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.mysimplechat.authorization.RegisterController;
import com.example.mysimplechat.chat.SimpleChat;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://192.168.1.5:5432/chatDB";
    private static final String USER = "chat_client";
    private static final String PASSWORD = "client_password";

    public static Connection getConnection() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server connection error");
            alert.setContentText("Please check your internet connection");
            alert.setHeaderText(null);
            alert.show();
            FXMLLoader fxmlLoader = new FXMLLoader(SimpleChat.class.getResource("register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            RegisterController registerController = fxmlLoader.getController();
            registerController.switchSceneOnLogin("", "");
//            e.printStackTrace(); // Выведет полный стек ошибок
        }
        return connection;
    }

    public static int saveUser(String username, String login, String hashedPassword) throws Exception {
        String query = "INSERT INTO users (username, login, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, login);
            stmt.setString(3, hashedPassword);
            return stmt.executeUpdate();
        }
    }

    public static String getPasswordHash(String login) throws Exception {
        String query = "SELECT password FROM users WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        }
        return null;
    }

    public static String getUsername(String login) throws Exception {
        String query = "SELECT username FROM users WHERE login = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    return rs.getString("username");
                }
            }
        }
        return null;
    }

    public static String getLogin(String username) throws Exception {
        String query = "SELECT login FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    return rs.getString("login");
                }
            }
        }
        return null;
    }

    public static boolean isUsernameExists(String username) throws Exception {
        String query = "SELECT login FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean isLoginExists(String login) throws Exception {
        String query = "SELECT login FROM users WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }


    public static void sendHeartbeat(String login) {
        String query = "UPDATE users SET last_ping = now() WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setIsConnected(String login, boolean isConnected) {
        String query = "UPDATE users SET is_connected = ? WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, isConnected);
            stmt.setString(2, login);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}