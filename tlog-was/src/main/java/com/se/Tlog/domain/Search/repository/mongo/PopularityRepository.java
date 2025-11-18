package com.se.Tlog.domain.Search.repository.mongo;

import com.se.Tlog.domain.Search.domain.DestinationPopularity;
import com.se.Tlog.domain.Travel.domain.Destination;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PopularityRepository extends MongoRepository<DestinationPopularity, String> {
    @Aggregation(pipeline = {
            "{ $sort: { score: -1 } }",
            "{ $limit: ?0 }",
            "{ $lookup: { from: 'destinations', localField: '_id', foreignField: '_id', as: 'dest' } }",
            "{ $unwind: { path: '$dest' } }",
            "{ $replaceWith: '$dest' }"
    })
    List<Destination> findPopularDestinations(int size);
}
