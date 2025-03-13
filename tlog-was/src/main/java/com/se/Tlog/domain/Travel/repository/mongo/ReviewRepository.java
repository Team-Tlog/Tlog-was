package com.se.Tlog.domain.Travel.repository.mongo;

import com.se.Tlog.domain.Travel.Entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    public List<Review> findByDestinationId(String destinationId);
}
