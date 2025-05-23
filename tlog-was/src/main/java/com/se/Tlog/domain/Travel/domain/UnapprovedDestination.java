package com.se.Tlog.domain.Travel.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "destinations_unapproved")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnapprovedDestination {
    @Id
    private String id;
    private UUID submitter;
    private Destination destination;
    private List<String> customTags = new ArrayList<String>();
    
    public static UnapprovedDestination create(UUID submitter, Destination destination, List<String> customTags) {
        if (submitter == null || destination == null)
            throw new IllegalArgumentException();
        
        return new UnapprovedDestination(submitter, destination, customTags);
    }
    
    private UnapprovedDestination(UUID submitter, Destination description, List<String> customTags) {
        this.submitter = submitter;
        this.destination = description;
        if (customTags != null)
            this.customTags.addAll(customTags);
    }
}
