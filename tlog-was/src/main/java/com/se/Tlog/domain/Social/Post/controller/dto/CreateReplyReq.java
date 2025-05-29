package com.se.Tlog.domain.Social.Post.controller.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 생성 데이터 형식입니다.")
public record CreateReplyReq(
        @Schema(description = "작성자 id")
        UUID author,
        
        @Schema(description = "댓글 내용")
        String content) {

}
