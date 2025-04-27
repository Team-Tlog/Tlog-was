package com.se.Tlog.domain.Travel.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import com.se.Tlog.domain.Travel.domain.Location;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDto {

    private String name;
    private String address;
    private Location location;
    private String city;
    private String district;
    private boolean hasParking;
    private boolean petFriendly;
    private String imageUrl;
    private String description;
    private List<TagIdDto> tags;
    
}
