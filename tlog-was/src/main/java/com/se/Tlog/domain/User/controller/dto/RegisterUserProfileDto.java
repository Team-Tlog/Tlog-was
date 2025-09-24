package com.se.Tlog.domain.User.controller.dto;

import java.util.List;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인시 입력해야하는 사용자 기본 정보입니다.")
public record RegisterUserProfileDto(
        @Schema(description = "TBTI 검사 결과. 00000000 ~ 99999999 사이의 값이어야 합니다.", example = "00137501")
        String tbtiValue,
        
        @Schema(description = "사용자가 선호하는 여행지 사진의 id입니다.", example = "[1, 3, 7]")
        List<Integer> preferPhotoIds) {

        public static void validate(RegisterUserProfileDto value) {
                if (value == null || value.tbtiValue == null)
                        throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        }
}
