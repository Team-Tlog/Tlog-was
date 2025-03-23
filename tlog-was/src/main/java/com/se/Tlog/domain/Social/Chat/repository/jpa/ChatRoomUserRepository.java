package com.se.Tlog.domain.Social.Chat.repository.jpa;

import com.se.Tlog.domain.Social.Chat.domain.ChatRoom;
import com.se.Tlog.domain.Social.Chat.domain.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, UUID> {

    @Query("SELECT cru.chatRoom from ChatRoomUser cru join fetch cru.chatRoom where cru.user.id = :userId")
    List<ChatRoom> findChatRoomByUserId(@Param("userId") UUID hostId);
}
