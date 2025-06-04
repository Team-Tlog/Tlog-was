package com.se.Tlog.domain.Review.controller.dto;

import java.util.Map;

import org.springframework.data.domain.Slice;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "특정 여행지의 리뷰를 조회한 데이터입니다.")
public record ReviewsRes(
        Map<Integer,Integer> ratingDistribution,
        Slice<DestinationReviewDto> reviews
        ) {

}
