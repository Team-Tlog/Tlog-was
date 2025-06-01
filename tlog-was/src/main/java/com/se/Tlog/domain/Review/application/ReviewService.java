package com.se.Tlog.domain.Review.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Review.controller.dto.DestinationReviewDto;
import com.se.Tlog.domain.Review.controller.dto.ReviewCreateDto;
import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.Review.domain.SortType;
import com.se.Tlog.domain.Review.repository.mongo.ReviewRepository;

import com.se.Tlog.domain.Travel.application.CustomTagService;
import com.se.Tlog.domain.Travel.domain.service.DestinationDomainService;
import com.se.Tlog.domain.User.controller.dto.UserProfileInfo;
import com.se.Tlog.domain.User.domain.service.UserDomainService;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.stream.Collectors;


@ApplicationService
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CustomTagService customTagService;
    private final DestinationDomainService destinationDomainService;
    private final UserDomainService userDomainService;

    public Slice<DestinationReviewDto> getReviewsByDestinationId(String destinationId, SortType sortType, Pageable pageable) {
        destinationDomainService.validateExists(destinationId);

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                getSort(sortType)
        );
        Slice<Review> reviews = reviewRepository.findByDestinationId(destinationId, sortedPageable);
        Set<UUID> uniqueUserIds = reviews.stream()
                .map(Review::getUserId)
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        Map<UUID,UserProfileInfo> userProfiles = userDomainService.getUserProfile(uniqueUserIds);

        List<DestinationReviewDto> dtoList = reviews.getContent().stream().map(review -> {
            UUID userId = UUID.fromString(review.getUserId());
            UserProfileInfo userProfileInfo = userProfiles.get(userId);
            return DestinationReviewDto.from(review, userProfileInfo);
        }).toList();

        return new SliceImpl<>(dtoList, sortedPageable, reviews.hasNext());
    }

    public void createReview(ReviewCreateDto reviewCreateDto) {
        Review review = Review.create(reviewCreateDto);
        reviewRepository.save(review);

        destinationDomainService.increaseReviewCountAndRating(reviewCreateDto.destinationId(), reviewCreateDto.rating());

        customTagService.addCustomTag(reviewCreateDto.destinationId(), reviewCreateDto.customTagNames());
    }

    public void deleteReview(String userId, String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorType.REVIEW_NOT_FOUND));

        if (!review.getUserId().equals(userId)) {
            throw new CustomException(ErrorType.UN_AUTHORIZATION);
        }
        destinationDomainService.decreaseReviewCount(review.getDestinationId());
        reviewRepository.delete(review);
    }

    private Sort getSort(SortType sortType) {
        return switch (sortType) {
            case RECENT -> Sort.by(Sort.Order.desc("createdAt"));
            case HIGH_SCORE -> Sort.by(Sort.Order.desc("rating"));
            case LOW_SCORE -> Sort.by(Sort.Order.asc("rating"));
        };
    }
}
