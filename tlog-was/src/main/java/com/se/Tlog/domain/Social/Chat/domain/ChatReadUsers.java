package com.se.Tlog.domain.Social.Chat.domain;

import com.se.Tlog.domain.User.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatReadUsers {//단체 채팅방에서 몇 명이 읽었는지
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "message_id")
    private ChatMessage message;

    private LocalDateTime readAt;

    private ChatReadUsers(User user, ChatMessage message) {
        this.user = user;
        this.message = message;
        this.readAt = LocalDateTime.now();
    }

    public static ChatReadUsers create(User user, ChatMessage message){
        return new ChatReadUsers(user, message);
    }

}
