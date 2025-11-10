package com.se.Tlog.domain.Guide.repository.mongo;

import com.se.Tlog.domain.Guide.repository.dto.RecomPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecPostRepository {
    private final MongoTemplate mongoTemplate;

    public List<RecomPost> findSome(int size) {
        Aggregation query = Aggregation.newAggregation(
                Aggregation.sample(size),
                Aggregation.project()
                        .and("_id").as("_id")
                        .and("content").as("content")
                        .and("imageUrls").as("imageUrls")
        );
        AggregationResults<RecomPost> results = mongoTemplate.aggregate(
                query,
                "sns_post",
                RecomPost.class
        );
        return results.getMappedResults();
    }
}
