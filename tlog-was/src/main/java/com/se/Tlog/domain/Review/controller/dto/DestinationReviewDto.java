package com.se.Tlog.domain.Review.controller.dto;

import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.User.controller.dto.UserProfileInfo;

import java.time.LocalDateTime;
import java.util.List;

public record DestinationReviewDto(
        String id,
        String userId,
        String username,
        String userProfileImageUrl,
        int rating,
        String content,
        List<String> reviewImageUrl,
        LocalDateTime createdAt
) {
    public static DestinationReviewDto from(
            Review review,
            UserProfileInfo userProfileInfo) {
        return new DestinationReviewDto(
                review.getId(),
                review.getUserId(),
                userProfileInfo.username(),
                userProfileInfo.imageUrl(),
                review.getRating(),
                review.getContent(),
                review.getImageUrlList(),
                review.getCreatedAt()
        );
    }
}
