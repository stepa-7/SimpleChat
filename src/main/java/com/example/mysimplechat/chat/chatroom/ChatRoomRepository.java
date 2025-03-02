package com.example.mysimplechat.chat.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository("chatRoomRepository")
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndReceiverId(String senderId, String receiverId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatRoom c WHERE (c.receiverId = :receiverId AND c.senderId = :senderId) " +
            "OR (c.receiverId = :senderId AND c.senderId = :receiverId)")
    void removeChatRoomByReceiverIdAndSenderId(@Param("receiverId") String receiverId,
                                               @Param("senderId") String senderId);
}