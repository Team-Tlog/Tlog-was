package com.se.Tlog.domain.Travel.controller.dto;

import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.Location;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record DestinationRes(
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
        List<TagIdDto> tags,
        Map<Integer,Integer> ratingDistribution
) {
    public static DestinationRes from(Destination destination) {
        List<TagIdDto> tagIdDtoList = destination.getTags().stream().filter(tagInfo -> !tagInfo.isDeleted())
                .map(tagInfo -> TagIdDto.from(tagInfo.getId(), tagInfo.getWeight()))
                .toList();
        System.out.println("destinationId = " + destination.getId());

        Map<Integer, Integer> distribution = new TreeMap<>();
        int[] ratingCount = destination.getRatingCount();
        for (int i = 0; i < 5; i++) {
            distribution.put(i+1, ratingCount[i]);
        }

        return new DestinationRes(
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
                tagIdDtoList,
                distribution
        );
    }
}
