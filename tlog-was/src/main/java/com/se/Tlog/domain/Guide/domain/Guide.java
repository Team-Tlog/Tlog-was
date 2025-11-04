package com.se.Tlog.domain.Guide.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    public static Guide create(
            String imageUrl,
            String title,
            String description,
            String property,
            double minLatitude,
            double minLongitude,
            double maxLatitude,
            double maxLongitude,
            String infoUrl
    ) {
        Guide guide = new Guide();
        guide.imageUrl = imageUrl;
        guide.title = title;
        guide.description = description;
        guide.property = property;
        guide.minLatitude = minLatitude;
        guide.minLongitude = minLongitude;
        guide.maxLatitude = maxLatitude;
        guide.maxLongitude = maxLongitude;
        guide.infoUrl = infoUrl;
        return guide;
    }
}
