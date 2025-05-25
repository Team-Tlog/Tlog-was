package com.se.Tlog.domain.Social.Chat.room.repository.jpa;

import com.se.Tlog.domain.Social.Chat.room.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByTeamId(UUID teamId);
}
