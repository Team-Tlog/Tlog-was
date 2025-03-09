package com.se.Tlog.domain.Travel.controller;

import com.se.Tlog.domain.Travel.Entity.Review;
import com.se.Tlog.domain.Travel.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.createReview(review));
    }

    @GetMapping("/{destinationId}")
    public ResponseEntity<List<Review>> getReviewsByDestinationId(@PathVariable String destinationId) {
        return ResponseEntity.ok(reviewService.getReviewsByDestinationId(destinationId));
    }
}