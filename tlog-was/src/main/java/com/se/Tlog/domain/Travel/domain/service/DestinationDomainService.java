package com.se.Tlog.domain.Travel.domain.service;

import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.repository.DestinationRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DestinationDomainService {
    private final DestinationRepository destinationRepository;
    private final MongoTemplate mongoTemplate;
    private final DestinationRepositoryService destinationRepositoryService;

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

        destinationRepositoryService.increaseReviewCountAndRating(destinationId, rating, approximateAverage);
    }

    public void decreaseReviewCountAndRating(String destinationId, Review review) {
        destinationRepositoryService.decreaseReviewCountAndRating(destinationId, review);
    }
    public boolean isApproved(String destinationId) {
        return destinationRepository.existsById(destinationId);
    }
}
