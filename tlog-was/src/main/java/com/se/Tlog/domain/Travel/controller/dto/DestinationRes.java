package com.se.Tlog.domain.Travel.controller.dto;

import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.Location;

import java.util.List;

public record DestinationRes(
        String id,
        String name,
        String address,
        Location location,
        int rating,
        String city,
        boolean hasParking,
        boolean petFriendly,
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
                destination.getRating(),
                destination.getCity(),
                destination.isHasParking(),
                destination.isPetFriendly(),
                tagIdDtoList
        );
    }
}
