package com.se.Tlog.domain.Travel.controller.dto;

public record TagRes(
        String tagId,
        String tagName,
        boolean deleted
) {
    public static TagRes from(String tagId, String tagName, boolean deleted) {
        return new TagRes(tagId, tagName, deleted);
    }
}
