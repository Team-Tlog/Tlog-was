package com.se.Tlog.domain.Search.repository.dto;

import com.se.Tlog.domain.Search.controller.dto.AiTagDetail;

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
            String imageUrl,
            List<AiTagDetail> tags,
            double similarity_score
    ) {
    }
}
