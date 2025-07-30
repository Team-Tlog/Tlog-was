package com.se.Tlog.domain.Travel.repository.mongo.dto;

import com.se.Tlog.domain.Travel.domain.Destination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DestinationOfTagProjection {
    private String id;
    private Destination selectedDest;
}
