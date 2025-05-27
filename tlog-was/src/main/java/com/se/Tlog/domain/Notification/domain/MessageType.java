package com.se.Tlog.domain.Notification.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

public enum MessageType {
    DEFAULT_STRING_MESSAGE(1),
    LINKABLE_MESSAGE(2),
    DEFAULT_SNS_MESSAGE(10),
    FOLLOW_MESSAGE(11);
    
    private final int code;
    MessageType(int code) { this.code = code; }
    public String code() { return Integer.toString(code); }
    
    static MessageType of(String code) {
        try {
            int parsedCode = Integer.parseInt(code);
            for (MessageType m : MessageType.values())
                if (m.code == parsedCode)
                    return m;
        } catch (Exception e) {
        
        }
        throw new CustomException(ErrorType.INVALID_TLOG_MESSAGE_TYPE);
    }
}
