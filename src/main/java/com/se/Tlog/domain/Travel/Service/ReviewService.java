package com.se.Tlog.domain.Travel.Service;

import com.se.Tlog.domain.Travel.Entity.Review;
import com.se.Tlog.domain.Travel.repository.ReviewRepository;
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
