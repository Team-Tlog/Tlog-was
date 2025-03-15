package com.se.Tlog.domain.User.presentation.dto;

import com.se.Tlog.domain.User.domain.SsoType;


public record LoginRequest(
        SsoType type,
        String accessToken
) {
}
