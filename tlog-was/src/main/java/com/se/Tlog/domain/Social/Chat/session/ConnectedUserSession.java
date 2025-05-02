package com.se.Tlog.domain.Social.Chat.session;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class ConnectedUserSession {
    @Id
    private String sessionId;

    private UUID userId;

    private Long roomId;

    private LocalDateTime connectedAt;
    private LocalDateTime disconnectedAt;

    private ConnectedUserSession(String sessionId,UUID userId, Long roomId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.roomId = roomId;
        this.connectedAt = LocalDateTime.now();
    }

    public static ConnectedUserSession create(String sessionId,UUID userId,Long roomId){
        return new ConnectedUserSession(sessionId, userId, roomId);
    }

    public void disconnected() {
        this.disconnectedAt = LocalDateTime.now();
    }
}
