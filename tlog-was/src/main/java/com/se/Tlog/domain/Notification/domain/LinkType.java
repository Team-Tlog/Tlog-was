package com.se.Tlog.domain.Notification.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

public enum LinkType {
    PAGE_MAIN(1),
    PAGE_MYPAGE(2),
    LINK_DESTINATION(10),
    LINK_POST(11),
    LINK_USER_SNS(12),
    LINK_CHAT_ROOM(13);
    
    private final int code;
    LinkType(int code) { this.code = code; }
    public String code() { return Integer.toString(code); }

    static LinkType of(String code) {
        try {
            int parsedCode = Integer.parseInt(code);
            for (LinkType m : LinkType.values())
                if (m.code == parsedCode)
                    return m;
        } catch (Exception e) {
        
        }
        throw new CustomException(ErrorType.INVALID_LINK_MESSAGE_TYPE);
    }
}
