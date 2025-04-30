package com.se.Tlog.domain.Review.controller.dto;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import java.util.List;

public record ReviewCreateDto(
        String userId,
        String destinationId,
        String username,
        int rating,
        String content,
        List<String> imageUrlList,
        List<String> customTagNames
) {
    public ReviewCreateDto{
        if (rating < 1 || rating > 5) {
            throw new CustomException(ErrorType.INVALID_REVIEW_RATING);
        }
        if (customTagNames == null) {
            customTagNames = List.of();
        }
        if (imageUrlList == null) {
            imageUrlList = List.of();
        }
    }
}
