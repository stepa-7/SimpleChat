package com.stepa7.webchat.repository;

import com.stepa7.webchat.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository("chatRoomRepository")
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findBySenderIdAndReceiverId(String senderId, String receiverId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Room c WHERE (c.receiverId = :receiverId AND c.senderId = :senderId) " +
            "OR (c.receiverId = :senderId AND c.senderId = :receiverId)")
    void removeChatRoomByReceiverIdAndSenderId(@Param("receiverId") String receiverId,
                                               @Param("senderId") String senderId);
}