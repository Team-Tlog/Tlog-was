package com.se.Tlog.domain.Travel.repository.mongo;

import com.mongodb.client.result.UpdateResult;
import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.DestinationSortType;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DestinationRepositoryExtension {
    private final MongoTemplate mongoTemplate;

    public void increaseReviewCountAndRating(String destinationId, int rating, float approximateAverage) {
        Update update = new Update()
                .inc("reviewCount", 1)
                .inc("ratingSum", rating)
                .inc("ratingCount." + (rating - 1), 1)
                .set("averageRating", approximateAverage);

        UpdateResult updateResult = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(destinationId)),
                update,
                Destination.class
        );

        if (updateResult.getMatchedCount() == 0) {
            throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
        }
    }

    public void decreaseReviewCountAndRating(String destinationId, Review review) {
        Destination destination = mongoTemplate.findById(destinationId, Destination.class);
        if (destination == null) {
            throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
        }

        int updatedReviewCount = destination.getReviewCount() - 1;
        float approximateAverage;
        if (updatedReviewCount <= 0) {  // 음수 방지
            approximateAverage = 0f;
        } else {
            approximateAverage = (float) (destination.getRatingSum() - review.getRating()) / updatedReviewCount;
        }

        UpdateResult updateResult = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(destinationId)),
                new Update().inc("reviewCount", -1)
                        .inc("ratingSum", -review.getRating())
                        .inc("ratingCount." + (review.getRating() - 1), -1)
                        .set("averageRating", approximateAverage)
                ,
                Destination.class
        );

        if (updateResult.getMatchedCount() == 0) {
            throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
        }
    }

    public List<Destination> getDestinations(Pageable pageable, String city, DestinationSortType sortType) {
        MatchOperation matchStage = Aggregation.match(Criteria.where("city").is(city));

        SortOperation sortStage = null;
        switch (sortType){
            case REVIEW -> { //평점 높은 순
                sortStage = Aggregation.sort(Sort.by(Sort.Order.desc("averageRating")));
            }
            case POPULAR -> { // 리뷰 갯수
                sortStage = Aggregation.sort(Sort.by(Sort.Order.desc("reviewCount")));
            }
        }
        sortStage = sortStage.and(Sort.by(Sort.Order.desc("_id")));

        SkipOperation skipStage = Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitStage = Aggregation.limit(pageable.getPageSize() + 1);  // 다음 페이지 존재 여부 확인 위해 +1

        Aggregation aggregation = Aggregation.newAggregation(
                matchStage,
                sortStage,
                skipStage,
                limitStage
        );

        return new ArrayList<>(mongoTemplate.aggregate(aggregation, "destinations", Destination.class)
                .getMappedResults());
    }
}
