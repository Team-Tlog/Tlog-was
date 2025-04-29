package com.se.Tlog.domain.Travel.controller.dto;

import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.Location;

import java.util.List;
import java.util.Map;

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
                Map.of(
                        1, destination.getRatingCount1(),
                        2, destination.getRatingCount2(),
                        3, destination.getRatingCount3(),
                        4, destination.getRatingCount4(),
                        5, destination.getRatingCount5()
                )
        );
    }
}
