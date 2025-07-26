package com.se.Tlog.domain.Social.Post.controller.dto;

public record LikeToggleRes(
        boolean liked
) {
    public static LikeToggleRes from(boolean liked) {
        return new LikeToggleRes(liked);
    }
}
