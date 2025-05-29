package com.se.Tlog.domain.Social.Post.controller.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "SNS 내 코스 리뷰(게시글)에 작성되는 댓글 조회 데이터형식입니다.")
public record ReplyRes(
        // 댓글 정보
        String replyId,
        String content,
        int nestedReplyCount,
        
        // 작성자 정보
        UUID authorId,
        String authorName,
        String authorProfileImageUrl) {

}
