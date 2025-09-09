package com.stepa7.webchat.repository;

import com.stepa7.webchat.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("chatMessageRepository")
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByChatId(String chatId);
}
