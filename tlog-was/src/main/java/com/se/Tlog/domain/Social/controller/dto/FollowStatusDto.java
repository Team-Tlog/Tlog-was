package com.se.Tlog.domain.Social.controller.dto;

public record FollowStatusDto(
        boolean status,
        String message
){
    public static FollowStatusDto from(boolean status) {
        String msg = status ? "팔로우 되었습니다." : "언팔로우 되었습니다.";
        return new FollowStatusDto(status,msg);
    }
}