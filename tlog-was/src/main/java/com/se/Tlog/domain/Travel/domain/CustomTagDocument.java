package com.se.Tlog.domain.Travel.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "customtags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomTagDocument {
    @Id
    private String id;

    private String destinationId;
    private Map<String, Integer> customTags = new HashMap<>();

    public static CustomTagDocument create(String destinationId) {
        return new CustomTagDocument(destinationId);
    }

    private CustomTagDocument(String destinationId) {
        this.destinationId = destinationId;
    }
}
