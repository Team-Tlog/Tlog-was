package com.se.Tlog.domain.User.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.User.domain.SsoType;
import com.se.Tlog.domain.User.presentation.dto.SsoUserInfo;

@ApplicationService
public interface SsoService {
    SsoUserInfo getUserInfo(String accessToken);
    SsoType getType();
}
