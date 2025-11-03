package com.se.Tlog.domain.Social.Chat.read.repository.jpa;

import com.se.Tlog.domain.Social.Chat.message.ChatMessage;
import com.se.Tlog.domain.Social.Chat.read.ChatReadUsers;
import com.se.Tlog.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatReadUsersRepository extends JpaRepository<ChatReadUsers,Long> {
    Boolean existsByUserAndMessage(User user, ChatMessage message);

    // 메시지별 읽음 수 조회 (효율적인 방법)
    @Query("SELECT cr.message.id, COUNT(cr) " +
            "FROM ChatReadUsers cr " +
            "WHERE cr.message.id IN :messageIds " +
            "GROUP BY cr.message.id")
    List<Object[]> countReadsByMessageIds(@Param("messageIds") List<Long> messageIds);

    // 특정 메시지의 읽음 수
    int countByMessage(ChatMessage message);

    // 메시지 ID로 읽음 수 조회
    @Query("SELECT COUNT(cr) FROM ChatReadUsers cr WHERE cr.message.id = :messageId")
    int countByMessageId(@Param("messageId") Long messageId);
}
