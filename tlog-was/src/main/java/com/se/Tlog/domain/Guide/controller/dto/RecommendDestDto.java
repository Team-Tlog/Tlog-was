package com.se.Tlog.domain.Guide.controller.dto;

import com.se.Tlog.domain.Guide.repository.dto.FullRecDest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendDestDto {
    @Schema(example = "https://example.com/image")
    private String imageUrl;
    @Schema(example = "이번 가을, 단풍 속에서 인생샷!")
    private String title;
    @Schema(example = "가을 풍경 명소 보러가기")
    private String description;
    private List<Dest> destinations;
    @Data
    @AllArgsConstructor
    public static class Dest {
        @Schema(example = "https://example.com/image")
        private String imageUrl;
        @Schema(example = "티로그마을 단풍길")
        private String name;
        @Schema(example = "여행지id")
        private String id;
    }

    public static RecommendDestDto create(FullRecDest recDest) {
        return new RecommendDestDto(
                recDest.getImageUrl(),
                recDest.getTitle(),
                recDest.getDescription(),
                recDest.getDestinations().stream()
                        .map(dest -> new Dest(
                                dest.getImageUrl(),
                                dest.getName(),
                                dest.getId()
                        )).toList()
        );
    }
}
