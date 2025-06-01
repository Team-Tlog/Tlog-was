package com.se.Tlog.domain.Travel.domain.service;

import com.mongodb.client.result.UpdateResult;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DestinationDomainService {
    private final DestinationRepository destinationRepository;
    private final MongoTemplate mongoTemplate;

    public void validateExists(String destinationId) {
        if (!destinationRepository.existsById(destinationId)) {
            throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
        }
    }

    public void increaseReviewCountAndRating(String destinationId, int rating) {
        Destination destination = mongoTemplate.findById(destinationId, Destination.class);
        if (destination == null) {
            throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
        }
        float approximateAverage = (float) (destination.getRatingSum() + rating) / (destination.getReviewCount());

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

    public void decreaseReviewCount(String destinationId) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(destinationId)),
                new Update().inc("reviewCount", -1),
                Destination.class
        );
    }
    public boolean isApproved(String destinationId) {
        return destinationRepository.existsById(destinationId);
    }
}
