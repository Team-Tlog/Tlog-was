package com.se.Tlog.domain.Review.controller.dto;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import java.util.List;

public record ReviewCreateDto(
        // 사용자 id를 uuid로 받는 것도 고려!
        String userId,
        String destinationId,
        @Deprecated(since = "사용자 정보는 더 이상 필요없습니다!")
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
