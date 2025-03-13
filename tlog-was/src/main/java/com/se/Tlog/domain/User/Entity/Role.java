package com.se.Tlog.domain.User.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER"),
    GUEST("ROLE_GUEST"),
    ADMIN("ROLE_ADMIN");

    private final String value;
}