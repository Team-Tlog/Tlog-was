package com.se.Tlog.domain.Course.controller.dto;

import com.se.Tlog.domain.Wishlist.domain.dto.WishlistDestinationRes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CourseRecommendationReq(
        @NotEmpty(message = "메인 지역(city)은 필수입니다.")
        String city,

        @NotEmpty(message = "상세 지역(regionList)은 최소 1개 이상 선택해야 합니다.")
        List<Integer> region_codes,

        @NotEmpty(message = "여행 일정(dailyPlans)은 최소 1일 이상 설정해야 합니다.")
        @Valid
        List<DayPlanDto> dailyPlans,

        List<WishlistDestinationRes> wishlist
) {
}
