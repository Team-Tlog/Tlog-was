package com.se.Tlog.domain.Social.Chat.repository.jpa;

import com.se.Tlog.domain.Social.Chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT count(cm) from ChatMessage cm where cm.chatRoom.id = :roomId and cm.id > :lastReadMessageId")
    int countUnreadMessages(@Param("roomId") Long roomId, @Param("lastReadMessageId") Long lastReadMessageId);

    ChatMessage findFirstByChatRoomIdOrderByIdDesc(Long roomId);
}
