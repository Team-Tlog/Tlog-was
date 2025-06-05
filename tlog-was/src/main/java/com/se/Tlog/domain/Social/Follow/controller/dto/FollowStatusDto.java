package com.se.Tlog.domain.Social.Follow.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FollowStatusDto(
        @Schema(example = "true")
        boolean status,
        @Schema(example = "팔로우 되었습니다. (status는 [갱신된 팔로우 상태]를 표시합니다.)")
        String message
){
    public static FollowStatusDto from(boolean status) {
        String msg = status ? "팔로우 되었습니다." : "언팔로우 되었습니다.";
        return new FollowStatusDto(status,msg);
    }
}