package com.se.Tlog.domain.Social.Chat.room.repository.jpa;

import com.se.Tlog.domain.Social.Chat.room.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
}
