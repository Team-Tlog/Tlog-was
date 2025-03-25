package com.se.Tlog.domain.Social.Chat.domain;

import com.se.Tlog.domain.User.domain.User;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatReadStatus { // 유저가 마지막으로 읽은 메시지 상태 관리
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    private Long lastReadMessageId;

    private LocalDateTime updatedAt;

    private ChatReadStatus(User user, ChatRoom chatRoom, Long lastReadMessageId, LocalDateTime updatedAt) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.lastReadMessageId = lastReadMessageId;
        this.updatedAt = updatedAt;
    }

    public static ChatReadStatus create(User user, ChatRoom room, Long messageId) {
        return new ChatReadStatus(user, room, messageId, LocalDateTime.now());
    }

    public void updateRead(Long messageId) {
        this.lastReadMessageId = messageId;
        this.updatedAt = LocalDateTime.now();
    }
}
