package com.se.Tlog.domain.Social.Chat.repository.jpa;

import com.se.Tlog.domain.Social.Chat.domain.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, UUID> {
}
