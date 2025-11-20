package com.se.Tlog.domain.Search.repository.dto;

import java.util.List;
import java.util.Map;

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
            String imageUrl,
            Map<String, Double> tagWeights,
            double similarity_score
    ) {
    }
}
