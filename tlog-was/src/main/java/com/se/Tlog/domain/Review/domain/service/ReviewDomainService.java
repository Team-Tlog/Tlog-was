package com.se.Tlog.domain.Review.domain.service;

import com.se.Tlog.domain.Review.controller.dto.DestinationReviewDto;
import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.Review.repository.mongo.ReviewRepository;
import com.se.Tlog.domain.User.controller.dto.UserProfileInfo;
import com.se.Tlog.domain.User.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewDomainService {
    private final ReviewRepository reviewRepository;
    private final UserDomainService userDomainService;

    public List<DestinationReviewDto> getTop2Reviews(String destinationId) {
        Pageable sortedPageable = PageRequest.of(0, 2, Sort.by(Sort.Order.desc("rating")).and(Sort.by(Sort.Order.desc("createAt"))));
        Slice<Review> reviews = reviewRepository.findByDestinationId(destinationId, sortedPageable);
        Set<UUID> uniqueUserIds = reviews.stream()
                .map(Review::getUserId)
                .map(UUID::fromString)
                .collect(Collectors.toSet());
        Map<UUID,UserProfileInfo> userProfiles = userDomainService.getUserProfileOnlyExist(uniqueUserIds);

        return reviews.stream().map(review -> {
            UUID userId = UUID.fromString(review.getUserId());
            UserProfileInfo userProfileInfo = userProfiles.get(userId);
            if (userProfileInfo != null)
                return DestinationReviewDto.from(review, userProfileInfo);
            else
                return DestinationReviewDto.from(review, UserProfileInfo.getNullProfile());
        }).toList();
    }
}
