package com.se.Tlog.domain.Search.repository.dto;

import java.util.List;

public record AiDestinationRes (
        List<AiDestination> destinations,
        int count
) {
    public record AiDestination(
            String id,
            String name,
            String description,
            String city,
            String district,
            List<String> tags,
            double similarity_score
    ) {
    }
}
