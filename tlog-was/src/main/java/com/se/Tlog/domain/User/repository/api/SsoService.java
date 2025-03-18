package com.se.Tlog.domain.User.repository.api;

import org.springframework.stereotype.Service;

import com.se.Tlog.domain.User.controller.dto.SsoUserInfo;
import com.se.Tlog.domain.User.domain.SsoType;

@Service
public interface SsoService {
    SsoUserInfo getUserInfo(String accessToken);
    SsoType getType();
}
