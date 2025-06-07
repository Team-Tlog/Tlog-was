package com.se.Tlog.domain.Social.Profile.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "SNS 프로필에 표시할 한 줄 설명글을 변경하는 데이터형식입니다.")
public record UpdateSnsDescriptionReq(
        String description) {

}
