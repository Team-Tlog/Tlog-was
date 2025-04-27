package com.se.Tlog.domain.Travel.controller.dto;

import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.Location;

import java.util.List;

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
        List<TagIdDto> tags
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
                tagIdDtoList
        );
    }
}
