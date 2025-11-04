package com.se.Tlog.domain.Guide.controller.dto;

import com.se.Tlog.domain.Guide.domain.Guide;
import io.swagger.v3.oas.annotations.media.Schema;

public record RawGuideDto(
        int id,
        String imageUrl,
        String title,
        String description,
        @Schema(example = "부산/지역행사/바다")
        String property,
        @Schema(example = "-360.0")
        double minLatitude,
        @Schema(example = "-360.0")
        double minLongitude,
        @Schema(example = "360.0")
        double maxLatitude,
        @Schema(example = "360.0")
        double maxLongitude,
        String infoUrl
) {
    public static RawGuideDto from(Guide guide) {
        return new RawGuideDto(
                guide.getId(),
                guide.getImageUrl(),
                guide.getTitle(),
                guide.getDescription(),
                guide.getProperty(),
                guide.getMinLatitude(),
                guide.getMinLongitude(),
                guide.getMaxLatitude(),
                guide.getMaxLongitude(),
                guide.getInfoUrl()
        );
    }
}
