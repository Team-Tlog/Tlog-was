package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.Travel.domain.Review;
import com.se.Tlog.domain.Travel.infrastructure.mongo.ReviewRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
