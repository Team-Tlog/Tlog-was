package com.se.Tlog.domain.User.repository.api;

import com.se.Tlog.domain.User.domain.SsoType;

public interface SsoOauthManager {
	public String getLoginUrl();
	public String getAccessToken(String code);
	public SsoType getType();
}
