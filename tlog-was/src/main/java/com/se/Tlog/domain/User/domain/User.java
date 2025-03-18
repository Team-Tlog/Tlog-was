package com.se.Tlog.domain.User.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.se.Tlog.domain.User.controller.dto.SsoUserInfo;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;


    String providerUserInfo;

    private String name;
    private String email;
    //private String telephoneNumber; 사용자가 동의하지 않은 경우 못받을 수 있음 nullable 하게 관리

    @Enumerated(EnumType.STRING)
    private Role role;

    private User(
            String name,
            String provider,
            String providerId,
            String email

    ) {
        this.name = name;
        this.providerUserInfo = provider + " " + providerId;
        this.email = email;
//        this.telephoneNumber = telephoneNumber;
        this.role = Role.USER;
    }
    public static User create(SsoUserInfo ssoUserInfo){
        return new User(ssoUserInfo.nickname(), ssoUserInfo.provider(),ssoUserInfo.providerId(), ssoUserInfo.email());
    }
}
