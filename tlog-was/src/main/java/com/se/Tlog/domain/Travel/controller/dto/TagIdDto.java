package com.se.Tlog.domain.Travel.controller.dto;

public record TagIdDto(
        String tagId,
        int weight
) {
    public static TagIdDto from(String tagId, int weight) {
        return new TagIdDto(tagId, weight);
    }
}


