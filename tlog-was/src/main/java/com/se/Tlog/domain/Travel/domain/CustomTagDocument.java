package com.se.Tlog.domain.Travel.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "customtags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomTagDocument {
    @Id
    private String id;

    private String destinationId;
    private List<TagCount> customTags = new ArrayList<>();

    public void addOrIncrement(List<String> tagNames) {
        for (String tagName : tagNames) {
            String normalizedTagName = tagName.trim().toLowerCase();

            TagCount existing = customTags.stream()
                    .filter(t -> t.getTagName().equals(normalizedTagName))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                existing.increment();
            }else{
                customTags.add(TagCount.create(normalizedTagName));
            }
        }
    }

    public static CustomTagDocument create(String destinationId) {
        return new CustomTagDocument(destinationId);
    }

    private CustomTagDocument(String destinationId) {
        this.destinationId = destinationId;
    }
}
