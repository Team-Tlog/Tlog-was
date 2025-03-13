package com.se.Tlog.domain.Travel.dto;

import com.se.Tlog.domain.Travel.Entity.Destination;
import com.se.Tlog.domain.Travel.Entity.Location;
import com.se.Tlog.domain.Travel.Entity.TagInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDto {

    private String name;
    private String address;
    private Location location;
    private int rating;
    private String city;
    private boolean hasParking;
    private boolean petFriendly;
    private List<TagIdDto> tags;

    public static Destination toEntity(DestinationDto destinationDto, List<TagInfo> tagInfoList) {
        return Destination.builder()
                .name(destinationDto.getName())
                .address(destinationDto.getAddress())
                .location(destinationDto.getLocation())
                .rating(destinationDto.getRating())
                .tags(tagInfoList)
                .city(destinationDto.getCity())
                .hasParking(destinationDto.isHasParking())
                .petFriendly(destinationDto.isPetFriendly())
                .build();
    }
}
