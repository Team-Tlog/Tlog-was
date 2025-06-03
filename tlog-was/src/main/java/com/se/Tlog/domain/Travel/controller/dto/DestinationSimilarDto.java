package com.se.Tlog.domain.Travel.controller.dto;

import com.se.Tlog.domain.Travel.domain.TagCount;

import java.util.List;

public record DestinationSimilarDto(
        String destinationId,
        String name,
        String imageUrl,
        String description,
        List<TagCount> customTags
) {
}
