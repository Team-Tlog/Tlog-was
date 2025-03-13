package com.se.Tlog.domain.User.dto;

import com.se.Tlog.domain.User.SsoType;


public record LoginRequest(
        SsoType type,
        String accessToken
) {
}
