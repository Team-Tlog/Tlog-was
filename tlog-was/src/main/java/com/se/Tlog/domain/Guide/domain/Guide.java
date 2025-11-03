package com.se.Tlog.domain.Guide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imageUrl;

    private String title;

    private String description;

    // '/' 문자로 구분합니다. (부산/지역맛집/등등)
    private String property;

    private double minLatitude;
    private double minLongitude;
    private double maxLatitude;
    private double maxLongitude;

    private String infoUrl;
}
