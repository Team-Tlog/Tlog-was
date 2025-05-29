package com.se.Tlog.domain.Social.Post.controller.dto;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코스 리뷰(게시글) 생성 데이터 형식입니다.")
public record CreatePostReq(
        @Schema(description = "작성자 id")
        UUID author,
        
        @Schema(description = "참조된 코스 id")
        String courseId, 
        
        @Schema(description = "본문 내용")
        String content, 
        
        @Schema(description = "이미지")
        List<String> imageUrls) {

}
