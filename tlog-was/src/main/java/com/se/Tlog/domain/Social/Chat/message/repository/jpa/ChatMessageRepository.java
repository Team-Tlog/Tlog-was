package com.se.Tlog.domain.Social.Chat.message.repository.jpa;

import com.se.Tlog.domain.Social.Chat.message.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT count(cm) from ChatMessage cm where cm.chatRoom.id = :roomId and cm.id > :lastReadMessageId")
    int countUnreadMessages(@Param("roomId") Long roomId, @Param("lastReadMessageId") Long lastReadMessageId);

    ChatMessage findFirstByChatRoomIdOrderByIdDesc(Long roomId);


    /*
    * 특정 채팅방의 특정 ID보다 이전 메시지만 최신순 정렬
    * */
    @Query("SELECT m FROM ChatMessage m " +
            "JOIN FETCH m.sender " +
            "JOIN FETCH m.chatRoom " +
            "WHERE m.chatRoom.id = :roomId " +
            "ORDER BY m.id DESC")
    List<ChatMessage> findMessagesBeforeCursor(
            @Param("roomId") Long roomId,
            @Param("beforeId") Long beforeId,
            Pageable pageable
    );

    @Query("SELECT m FROM ChatMessage m " +
            "JOIN FETCH m.sender " +
            "JOIN FETCH m.chatRoom " +
            "WHERE m.chatRoom.id = :roomId " +
            "ORDER BY m.id DESC")
    List<ChatMessage> findRecentMessages(
            @Param("roomId") Long roomId,
            Pageable pageable
    );
}
