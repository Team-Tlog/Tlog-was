package com.se.Tlog.domain.Review.repository.mongo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Review.domain.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Slice<Review> findByDestinationId(String destinationId, Pageable pageable);
}
