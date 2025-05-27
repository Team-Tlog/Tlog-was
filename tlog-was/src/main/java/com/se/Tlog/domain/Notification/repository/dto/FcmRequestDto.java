package com.se.Tlog.domain.Notification.repository.dto;

import java.util.Map;
import java.util.UUID;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.Getter;

@Getter
public class FcmRequestDto {
    private UUID userId;
    private Map<String, String> payload;
    
    public FcmRequestDto(UUID userId, Map<String, String> payload) {
        if (userId == null || payload.values().stream().anyMatch(v -> v == null))
            throw new CustomException(ErrorType.NULL_IN_FCM_MESSAGE);
        this.userId = userId;
        this.payload = payload;
    }
}
