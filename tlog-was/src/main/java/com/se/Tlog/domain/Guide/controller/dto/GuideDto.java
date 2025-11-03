package com.se.Tlog.domain.Guide.controller.dto;

import com.se.Tlog.domain.Guide.domain.Guide;

import java.util.List;

public record GuideDto(
        String imageUrl,
        String title,
        String description,
        List<String>property,
        String infoUrl
) {
    public static GuideDto from(Guide guide) {
        return new GuideDto(
                guide.getImageUrl(),
                guide.getTitle(),
                guide.getDescription(),
                List.of(guide.getProperty().split("/")),
                guide.getInfoUrl()
        );
    }
}
