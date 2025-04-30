package com.se.Tlog.domain.Travel.controller.dto;

import com.se.Tlog.domain.Review.controller.dto.DestinationReviewDto;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.Location;
import com.se.Tlog.domain.Travel.domain.TagCount;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record DestinationDetailsRes(
        String id,
        String name,
        String address,
        Location location,
        String city,
        String district,
        boolean hasParking,
        boolean petFriendly,
        int ratingSum,
        int reviewCount,
        float averageRating,
        String imageUrl,
        List<TagCount> topTags,
        Map<Integer,Integer> ratingDistribution,
        List<DestinationReviewDto> top2Reviews
) {
    public static DestinationDetailsRes from(Destination destination, List<TagCount> topTags, List<DestinationReviewDto> top2Reviews) {
        Map<Integer, Integer> distribution = new TreeMap<>();
        int[] ratingCount = destination.getRatingCount();
        for (int i = 0; i < 5; i++) {
            distribution.put(i+1, ratingCount[i]);
        }

        return new DestinationDetailsRes(
                destination.getId(),
                destination.getName(),
                destination.getAddress(),
                destination.getLocation(),
                destination.getCity(),
                destination.getDistrict(),
                destination.isHasParking(),
                destination.isPetFriendly(),
                destination.getRatingSum(),
                destination.getReviewCount(),
                destination.getAverageRating(),
                destination.getImageUrl(),
                topTags,
                distribution,
                top2Reviews
        );
    }
}
