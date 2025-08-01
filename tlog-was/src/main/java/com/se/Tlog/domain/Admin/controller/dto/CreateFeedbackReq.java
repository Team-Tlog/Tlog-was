package com.se.Tlog.domain.Admin.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

import java.util.List;

public record CreateFeedbackReq(
        /* // 이 필드는 jwt Token을 통해 받을 수 있는 정보이므로 생략합니다.
         * @Schema(description = "피드백을 생성한 사용자")
         * UUID userId
         */
        @Schema(example = "조회가 안됩니다")
        String title,
        @Schema(example = "이전에 다녀온 여행지를 조회하고 싶은데 표시되지 않습니다 고쳐주세요")
        String content,
        @Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "[\"imageUrl1\", \"imageUrl2\"]")
        List<String> refImageUrls
) {
}
