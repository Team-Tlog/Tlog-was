package com.se.Tlog.domain.Social.Chat.repository.jpa;

import com.se.Tlog.domain.Social.Chat.domain.ChatReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatReadStatusRepository extends JpaRepository<ChatReadStatus,Long> {


    @Query("select crs from ChatReadStatus crs join fetch crs.chatRoom " +
            "where crs.user.id = :userId and crs.chatRoom.id IN :chatRoomIds")
    List<ChatReadStatus> findByUserIdAndChatRoomIds(@Param("userId") UUID userId,
                                                    @Param("chatRoomIds") List<Long> chatRoomIds);
}
