package com.se.Tlog.domain.Travel.controller.dto;


import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.TagCount;

import java.util.List;

public record SimpleDestinationRes(
        String id,
        String name,
        String imageUrl,
        String description,
        List<TagCount> tagCountList
) {
    public static SimpleDestinationRes from(Destination destination, List<TagCount> topTags) {
        return new SimpleDestinationRes(
                destination.getId(),
                destination.getName(),
                destination.getImageUrl(),
                destination.getDescription(),
                topTags
        );
    }
}
