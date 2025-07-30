package com.se.Tlog.domain.Travel.repository.mongo;

import com.mongodb.client.result.UpdateResult;
import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.DestinationSortType;
import com.se.Tlog.domain.Travel.repository.mongo.dto.DestinationOfTagProjection;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Floor;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Multiply;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.ArrayElemAt;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Size;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    
    public Map<String, Destination> findAllByEachTags(List<String> tagsId) {
        /*      // 25.7.29
                // 쿼리 테스트를 위한 동일기능의 JSON형태 쿼리
                [
                    { $unwind: '$tags' },
                    { $match: { 'tags._id': { $in: [  ] } } },
                    { $group: { _id: '$tags._id', dests: { $push: '$_id' } } },
                    { 
                        $project: {
                            selectedDestId: {
                                $arrayElemAt: [
                                    // 여행지
                                    '$dests',
                                    // 랜덤 인덱스 추출
                                    { $floor: { $multiply: [ { $rand: {} }, { $size: '$dests' } ] } }
                                ]
                            }
                        }
                    },
                    { 
                        $lookup: { 
                            from: 'destinations', 
                            localField: 'selectedDestId',
                            foreignField: '_id',
                            as: 'selectedDest' 
                        } 
                    },
                    { $project: { _id:1, selectedDest: { $arrayElemAt: [ "$selectedDest" , 0 ] } } }
                ]
        */
        UnwindOperation pipe_unwind = Aggregation.unwind("tags");
        MatchOperation pipe_match = Aggregation.match(Criteria.where("tags._id").in(tagsId.stream().map(ObjectId::new).toList()));
        GroupOperation pipe_group = Aggregation.group("tags._id")
                                    .push("_id").as("dests");
        ProjectionOperation pipe_project1 = Aggregation.project()
                                    .and(ArrayElemAt.arrayOf("dests")
                                                    .elementAt(
                                                            Floor.floorValueOf(
                                                                Multiply
                                                                    .valueOf(ArithmeticOperators.rand())
                                                                    .multiplyBy(Size.lengthOfArray("dests")))))
                                    .as("selectedDestId");
        LookupOperation pipe_lookup = Aggregation.lookup("destinations", "selectedDestId", "_id", "selectedDest");
        ProjectionOperation pipe_project2 = Aggregation.project()
                                    .andInclude("_id")
                                    .and("selectedDest").arrayElementAt(0).as("selectedDest");
        
        Aggregation aggregation = Aggregation.newAggregation(
                pipe_unwind, 
                pipe_match, 
                pipe_group, 
                pipe_project1,
                pipe_lookup,
                pipe_project2);
        
        AggregationResults<DestinationOfTagProjection> results = mongoTemplate.aggregate(aggregation, "destinations", DestinationOfTagProjection.class);
        return results.getMappedResults().stream()
                .collect(Collectors.toMap(
                        dto -> dto.getId(),
                        dto -> dto.getSelectedDest()));
    }
}
