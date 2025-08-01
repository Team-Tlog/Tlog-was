package com.se.Tlog.domain.Admin.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateReportToPostReq(
        @Schema(description = "신고할 게시글의 id", example = "1a2b3c4d5e6f7g8h9i0j1k2l")
        String postId

        /* // 추후 확장시 본문도 포함가능합니다.
        private String title;

        private String content;
         */
) {
}
