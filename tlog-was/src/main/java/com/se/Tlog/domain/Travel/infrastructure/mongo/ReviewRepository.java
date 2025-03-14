package com.se.Tlog.domain.Travel.infrastructure.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.se.Tlog.domain.Travel.domain.Review;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    public List<Review> findByDestinationId(String destinationId);
}
