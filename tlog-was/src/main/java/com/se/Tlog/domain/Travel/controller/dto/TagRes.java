package com.se.Tlog.domain.Travel.controller.dto;

public record TagRes(
        String tagId,
        String tagName
) {
    public static TagRes from(String tagId, String tagName) {
        return new TagRes(tagId, tagName);
    }
}
