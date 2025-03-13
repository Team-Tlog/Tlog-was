package com.se.Tlog.global.security.dto;

import com.se.Tlog.domain.Admin.entity.Admin;
import com.se.Tlog.domain.User.Entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class AdminDetails implements CustomUserDetails{
    private final Admin admin;

    @Override
    public String getId() {
        return admin.getId().toString();
    }

    @Override
    public boolean hasRole(Role role) {
        return role.equals(admin.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(admin.getRole().getValue()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getAdminId();
    }
}
