package com.se.Tlog.domain.Review.infrastructure.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.se.Tlog.domain.Review.domain.Review;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    public List<Review> findByDestinationId(String destinationId);
}
