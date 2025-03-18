package com.se.Tlog.domain.User.controller.dto;

import lombok.Builder;

@Builder
public record TokenDto(
        String accessToken,
        String refreshToken
) { }
