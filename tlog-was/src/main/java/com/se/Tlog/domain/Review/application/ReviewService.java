package com.se.Tlog.domain.Review.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.Review.infrastructure.mongo.ReviewRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> getReviewsByDestinationId(String destinationId) {
        return reviewRepository.findByDestinationId(destinationId);
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }
}
