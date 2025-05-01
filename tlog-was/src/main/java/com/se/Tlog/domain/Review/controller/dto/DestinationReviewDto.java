package com.se.Tlog.domain.Review.controller.dto;

import com.se.Tlog.domain.Review.domain.Review;

import java.time.LocalDateTime;

public record DestinationReviewDto(
        String id,
        String userId,
        String username,
        int rating,
        String content,
        LocalDateTime createdAt
) {
    public static DestinationReviewDto from(Review review) {
        return new DestinationReviewDto(
                review.getId(),
                review.getUserId(),
                review.getUsername(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}
