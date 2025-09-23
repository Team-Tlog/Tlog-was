package com.se.Tlog.domain.User.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "preferPhotos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferPhoto {
    @Id
    private int id;

    private String imageUrl;

    private List<String> tagIds;
}
