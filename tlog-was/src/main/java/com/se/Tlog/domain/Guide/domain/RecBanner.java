package com.se.Tlog.domain.Guide.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recommend_banner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecBanner {
    @Id
    private String id;

    private String imageUrl;

    private String title;

    private List<String> hashTags;

    private BannerType bannerType;

    // ------ inner data ------

    private String relatedUrl;
    private List<String> destinationIds;
}
