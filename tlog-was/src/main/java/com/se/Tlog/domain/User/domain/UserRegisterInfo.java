package com.se.Tlog.domain.User.domain;

import com.se.Tlog.domain.Tbti.domain.Tbti;
import com.se.Tlog.domain.User.controller.dto.RegisterUserProfileDto;
import com.se.Tlog.domain.User.controller.dto.SsoUserInfo;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.Getter;

@Getter
public class UserRegisterInfo {
    private String nickname;
    private String email;
    private String providerUserInfo;
    private String profileImage;
    private Tbti tbti;
    
    public UserRegisterInfo(SsoUserInfo ssoUserInfo, RegisterUserProfileDto userProfiles) {
        if (userProfiles == null || ssoUserInfo == null
                || ssoUserInfo.getProviderUserInfo() == null || ssoUserInfo.getProviderUserInfo().trim().isEmpty()
                || ssoUserInfo.nickname() == null || ssoUserInfo.nickname().trim().isEmpty())
            throw new CustomException(ErrorType.INSUFFICIENT_INFO_FOR_REGISTER);
        
        this.nickname = ssoUserInfo.nickname();
        this.email = ssoUserInfo.email();
        this.providerUserInfo = ssoUserInfo.getProviderUserInfo();
        this.profileImage = "";
        try {
            this.tbti = new Tbti(Integer.parseInt(userProfiles.tbtiValue()));
        } catch (Exception e) {
            throw new CustomException(ErrorType.INVALID_TBTI_CODE);
        }
    }
    
    public void setEmail(String email) {
        // email 형식 검증
        this.email = email;
    }
    
    public void setProfileImage(String profileImage) {
        if (profileImage == null || profileImage.trim().isEmpty())
            throw new CustomException(ErrorType.INVALID_IMAGE_URL);
        this.profileImage = profileImage;
    }
}
