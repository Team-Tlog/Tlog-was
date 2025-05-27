package com.se.Tlog.domain.Notification.repository.dto;

import java.util.Map;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.Getter;

@Getter
public class FcmMessageDto {
    private String fcmToken;
    private Map<String, String> payload;
    
    public FcmMessageDto(String fcmToken, Map<String, String> payload) {
        if (fcmToken == null || payload.values().stream().anyMatch(v -> v == null))
            throw new CustomException(ErrorType.NULL_IN_FCM_MESSAGE);
        this.fcmToken = fcmToken;
        this.payload = payload;
    }
}
