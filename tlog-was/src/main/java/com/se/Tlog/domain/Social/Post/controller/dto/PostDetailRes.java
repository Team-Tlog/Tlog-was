package com.se.Tlog.domain.Social.Post.controller.dto;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코스 리뷰(게시물)의 전체 데이터 형식입니다.")
public record PostDetailRes(
        // 코스 리뷰(게시물) 정보
        String postId,
        int postLikeCount,
        @Schema(description = "코스 리뷰(게시물)의 공유 코드 (현재는 게시물 고유 id로 반환함) (url 구성 방법은 추후 논의)")
        String postLinkCode,
        
        // 코스 정보
        String courseId,
        List<String> courseDistrics,
        
        // 작성자 정보
        UUID authorId,
        String authorName,
        String authorProfileImageUrl,
        
        // 내용 정보
        List<String> contentImageUrls,
        String content) {

}
