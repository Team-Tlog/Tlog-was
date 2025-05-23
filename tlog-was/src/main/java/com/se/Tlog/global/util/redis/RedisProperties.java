package com.se.Tlog.global.util.redis;

public interface RedisProperties {
    String REFRESH_TOKEN_PREFIX = "refresh_token:";
    String ACCESS_TOKEN_PREFIX = "access_token:";
    /*
     * 25.05.22
     * 태그 처리되지 않고 검수되지 않은 여행지는
     * Redis로 기록되지 않고 별도의 DB공간에 기록되도록 변경되었습니다. 
     * String PENDING_TAGGING_DESTINATION = "pending:tagging:destination";
     */ 
}
