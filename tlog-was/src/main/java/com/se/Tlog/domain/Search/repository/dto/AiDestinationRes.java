package com.se.Tlog.domain.Search.repository.dto;

import com.se.Tlog.domain.Travel.domain.Location;

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
            Location location,
            String imageUrl,
            Map<String, Double> tagWeights,
            double similarity_score
    ) {
    }
}
