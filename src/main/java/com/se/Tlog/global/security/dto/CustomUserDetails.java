package com.se.Tlog.global.security.dto;

import com.se.Tlog.domain.User.Entity.Role;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    String getId();

    boolean hasRole(Role role);
}
