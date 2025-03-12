package com.se.Tlog.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessType {

    // 200
    OK(HttpStatus.OK, "요청이 성공했습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
    LOGIN_SSO_SUCCESS(HttpStatus.OK, "소셜 로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공하였습니다."),
    REISSUE_TOKEN_SUCCESS(HttpStatus.OK, "토큰 재발급에 성공하였습니다."),
    AVAILABLE_ID(HttpStatus.OK, "사용가능한 아이디입니다."),

    // 201
    CREATED(HttpStatus.CREATED, "등록을 성공하였습니다."),
    TAG_CREATED(HttpStatus.CREATED, "새로운 태그 등록을 성공하였습니다."),
    DESTINATION_CREATED(HttpStatus.CREATED,"새로운 여행지 등록을 성공하였습니다."),
    USER_CREATED(HttpStatus.CREATED, "사용자 회원가입을 성공하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    public int getStatusCode(){
        return status.value();
    }
}

