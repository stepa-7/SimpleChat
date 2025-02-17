package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.scene.control.Alert;

import java.sql.SQLException;

public class DatabaseUtil {
//    private static final String URL = "jdbc:postgresql://192.168.28.124:5432/chatDB";
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


}