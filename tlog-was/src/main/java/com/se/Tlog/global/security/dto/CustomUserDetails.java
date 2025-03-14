package com.se.Tlog.global.security.dto;

import org.springframework.security.core.userdetails.UserDetails;

import com.se.Tlog.domain.User.domain.Role;

public interface CustomUserDetails extends UserDetails {
    String getId();

    boolean hasRole(Role role);
}
