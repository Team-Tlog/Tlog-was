package com.se.Tlog.global.security.dto;

import com.se.Tlog.domain.User.Entity.Role;
import com.se.Tlog.domain.User.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class AppUserDetails implements CustomUserDetails{
    private final User user;

    @Override
    public String getId() {
        return user.getId().toString();
    }

    @Override
    public boolean hasRole(Role role) {
        return role.equals(user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getValue()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return "";
    }
}
