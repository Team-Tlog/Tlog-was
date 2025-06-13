package com.se.Tlog.domain.Review.repository.mongo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Review.domain.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    // 버그 위험성 : 순서가 보장되지 않을 경우 다음 페이지에 이전 페이지의 항목이 표시될 수 있습니다.
    // @Query(sort = "{'_id' : 1}") 가 추가될 것!
    Slice<Review> findByDestinationId(String destinationId, Pageable pageable);
}
