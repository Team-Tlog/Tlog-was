package com.se.Tlog.domain.Guide.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendPostDto {
    @Schema(example = "[\"https://example.com/image1\", \"https://example.com/image2\"]")
    private List<String> imageUrls;
    @Schema(example = "5월에 가본 여행지~")
    private String title;
    @Schema(example = "최근 친적집 방문 겸 ~에 같이 다녀왔어요")
    private String description;
    @Schema(example = "게시글ID")
    private String id;
}
