package com.se.Tlog.domain.Guide.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recommend_destination")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecDest {
    @Id
    private String id;

    private String imageUrl;

    private String title;

    private String description;

    private List<String> destinationIds;
}
