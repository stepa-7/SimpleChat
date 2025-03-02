package com.example.mysimplechat.chat;

import com.example.database.DatabaseUtil;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class UserStatusChecker {
    @Scheduled(fixedRate = 60000)
    public void checkUsersConnection() {
        String query = "UPDATE users SET is_connected = false WHERE last_ping < ?";
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.from(Instant.now().minusSeconds(120)));
            stmt.executeUpdate();
            System.out.println("Updated users connection status");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void closeAllConnections() {
        String query = "UPDATE users SET is_connected = false WHERE is_connected = true";
        try (Connection con = DatabaseUtil.getConnection();
        PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
