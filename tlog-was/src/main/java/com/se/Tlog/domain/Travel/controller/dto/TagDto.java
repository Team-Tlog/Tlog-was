package com.se.Tlog.domain.Travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "생성할 태그 name 요청 Dto")
public record TagDto(
        @Schema(description = "생성할 태그 name")
        String name
) {}
