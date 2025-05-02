package com.se.Tlog.domain.Social.Chat.room.repository.jpa;


import com.se.Tlog.domain.Social.Chat.room.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, UUID> {

    @Query("SELECT cru from ChatRoomUser cru join fetch cru.chatRoom where cru.user.id = :userId")
    List<ChatRoomUser> findChatRoomByUserId(@Param("userId") UUID hostId);

    @Query("select count(cru) from ChatRoomUser cru where cru.chatRoom.id = :roomId")
    int countChatRoomJoinUsers(@Param("roomId") Long roomId);
}
