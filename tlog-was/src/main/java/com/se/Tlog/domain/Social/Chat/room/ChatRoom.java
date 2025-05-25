package com.se.Tlog.domain.Social.Chat.room;

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
    private UUID teamId;

    private Long lastChatId;

    private LocalDateTime createAt = LocalDateTime.now();
    private LocalDateTime deleteAt;


    private ChatRoom(UUID hostId,UUID teamId){
        this.teamId = teamId;
        this.hostId = hostId;
    }

    public static ChatRoom create(UUID hostId,UUID teamId) {
        return new ChatRoom(hostId,teamId);
    }

    public void modifyLastMessage(Long lastChatId) {
        this.lastChatId = lastChatId;
    }

    public void deleteChatRoom() {
        this.deleteAt = LocalDateTime.now();
    }
}
