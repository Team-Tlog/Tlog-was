package com.se.Tlog.domain.Social.Post.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코스 리뷰(게시물)의 미리보기 데이터 형식입니다.")
public record PostPreviewRes(
        @Schema(description = "코스 리뷰(게시물) id")
        String postId,
        
        @Schema(description = "간략히 보기 대표사진")
        String previewImageUrl) {

}
