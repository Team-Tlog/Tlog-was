package com.se.Tlog.domain.User.Service;

import com.se.Tlog.domain.User.SsoType;
import com.se.Tlog.domain.User.dto.SsoUserInfo;

public interface SsoService {
    SsoUserInfo getUserInfo(String accessToken);
    SsoType getType();
}
