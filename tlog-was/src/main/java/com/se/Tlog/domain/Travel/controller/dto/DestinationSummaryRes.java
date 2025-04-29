package com.se.Tlog.domain.Travel.controller.dto;

import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.Location;
import com.se.Tlog.domain.Travel.domain.TagCount;

import java.util.List;

public record DestinationSummaryRes(
        String id,
        String name,
        String city,
        Location location,
        int reviewCount,
        float averageRating,
        String imageUrl,
        List<TagCount> tagCountList
        ) {

    public static DestinationSummaryRes from(Destination destination, List<TagCount> topTags) {
        return new DestinationSummaryRes(
                destination.getId(),
                destination.getName(),
                destination.getCity(),
                destination.getLocation(),
                destination.getReviewCount(),
                destination.getAverageRating(),
                destination.getImageUrl(),
                topTags
        );
    }
}
