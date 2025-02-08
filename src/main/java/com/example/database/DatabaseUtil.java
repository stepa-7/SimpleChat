package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.example.mysimplechat.*;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://192.168.28.124:5432/chatDB";
    private static final String USER = "chat_client";
    private static final String PASSWORD = "client_password";

    // Получение соединения с базой данных
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
            e.printStackTrace(); // Выведет полный стек ошибок
        }
        return connection;
    }

    // Сохранение нового пользователя с хешированным паролем
    public static void saveUser(String username, String login, String hashedPassword) throws Exception {
        String query = "INSERT INTO users (username, login, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, login);
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();
        }
    }

    // Получение хеша пароля для пользователя (для аутентификации)
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
}