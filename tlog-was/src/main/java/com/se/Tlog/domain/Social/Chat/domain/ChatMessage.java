package com.se.Tlog.domain.Social.Chat.domain;

import com.se.Tlog.domain.User.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatRoom_id", nullable = false)
    private ChatRoom chatRoom;

    private String content;
    private boolean isChecked;  // 메시지 확인여부

    private LocalDateTime sendAt;

    private ChatMessage(User sender, ChatRoom chatRoom, String content) {
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.content = content;
        this.sendAt = LocalDateTime.now();
    }

    public static ChatMessage create(User sender, ChatRoom chatRoom, String content) {
        return new ChatMessage(sender, chatRoom, content);
    }
}
