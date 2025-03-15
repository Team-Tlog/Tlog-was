package com.se.Tlog.domain.User.infrastructure.api;

import org.springframework.stereotype.Service;

import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.domain.User.presentation.dto.SsoUserInfo;

@Service
public interface SsoService {
    SsoUserInfo getUserInfo(String accessToken);
    SsoType getType();
}
