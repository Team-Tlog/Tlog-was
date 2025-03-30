package com.se.Tlog.domain.Social.Chat.service;

import com.se.Tlog.domain.Social.Chat.domain.ConnectedUserSession;
import com.se.Tlog.domain.Social.Chat.repository.jpa.ConnectedUserSessionRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConnectedUserSessionService {
    private final ConnectedUserSessionRepository connectedUserSessionRepository;

    public void connect(String sessionId, UUID userId, Long roomId) {
        ConnectedUserSession connectedUserSession = ConnectedUserSession.create(sessionId, userId, roomId);
        connectedUserSessionRepository.save(connectedUserSession);
    }


    public void disconnect(String sessionId) {
        ConnectedUserSession connectedUserSession = connectedUserSessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        connectedUserSession.disconnected();

        connectedUserSessionRepository.deleteById(sessionId);
    }
}
