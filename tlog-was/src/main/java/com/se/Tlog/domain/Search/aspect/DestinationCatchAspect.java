package com.se.Tlog.domain.Search.aspect;

import com.se.Tlog.domain.Review.controller.dto.ReviewCreateDto;
import com.se.Tlog.domain.Search.repository.redis.PopularityCacheRepository;
import com.se.Tlog.domain.Wishlist.domain.UpdateType;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DestinationCatchAspect {
    private final PopularityCacheRepository popularityCacheRepository;

    private void addDestinationScore(String destinationId, int addScore) {
        popularityCacheRepository.addScore(destinationId, addScore);
    }

    private void addDestinationScore(String destinationId) {
        addDestinationScore(destinationId, 1);
    }

    // 여행지를 세부 조회한 경우 점수 + 1
    @AfterReturning("execution(* *..DestinationService.getDestinationById(..)) && args(destinationId)")
    public void detailedDestination(String destinationId) {
        addDestinationScore(destinationId);
    }

    // 장바구니/스크랩에 추가한 경우 점수 + 1
    @AfterReturning("execution(* *..WishlistService.updateWishlist(..)) && args(updateType, *, *, destinationId)")
    public void scrappedDestination(UpdateType updateType, String destinationId) {
        if (updateType == UpdateType.ADD)
            addDestinationScore(destinationId);
    }

    // 리뷰에 긍정적인 평가 점수 + 1~3 (별3개: 1, 별4개: 2, 별5개: 3)
    @AfterReturning("execution(* *..ReviewService.createReview(..)) && args(reviewCreateDto)")
    public void reviewedDestination(ReviewCreateDto reviewCreateDto) {
        if (3 <= reviewCreateDto.rating())
            addDestinationScore(reviewCreateDto.destinationId(), reviewCreateDto.rating() - 2);
    }
}
