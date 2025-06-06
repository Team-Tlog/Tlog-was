package com.se.Tlog.domain.User.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.se.Tlog.domain.Tbti.domain.Tbti;

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

    private String snsId;
    
    private int tbti;
    //private String telephoneNumber; 사용자가 동의하지 않은 경우 못받을 수 있음 nullable 하게 관리

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    private User(
            String name,
            String providerUserInfo,
            String email,
            int tbti

    ) {
        this.name = name;
        this.providerUserInfo = providerUserInfo;
        this.email = email;
//        this.telephoneNumber = telephoneNumber;
        this.role = Role.USER;
        this.tbti = tbti;
    }
    public static User create(UserRegisterInfo userRegisterInfo){
        return new User(
                userRegisterInfo.getNickname(),
                userRegisterInfo.getProviderUserInfo(), 
                userRegisterInfo.getEmail(), 
                userRegisterInfo.getTbti().getTbtiCode());
    }
    
    public String getTbtiString() {return new Tbti(tbti).toString();}

    public void updateEmail(String email) {this.email = email;}
    public void updateSnsId(String snsId) {this.snsId = snsId;}
    public void updateProfileImage(String profileImage) {this.profileImage = profileImage;}
}
