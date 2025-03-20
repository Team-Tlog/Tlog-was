package com.se.Tlog.domain.Social.Chat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID hostId; //채팅방 생성한 유저

    private Long lastChatId;

    private LocalDateTime createAt = LocalDateTime.now();
    private LocalDateTime deleteAt;


    private ChatRoom(UUID hostId){
        this.hostId = hostId;
    }

    public static ChatRoom create(UUID hostId) {
        return new ChatRoom(hostId);
    }

    public void modifyLashMessage(Long lastChatId) {
        this.lastChatId = lastChatId;
    }

    public void deleteChatRoom() {
        this.deleteAt = LocalDateTime.now();
    }
}
