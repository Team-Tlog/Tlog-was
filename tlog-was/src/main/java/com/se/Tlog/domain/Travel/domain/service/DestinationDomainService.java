package com.se.Tlog.domain.Travel.domain.service;

import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepositoryExtension;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DestinationDomainService {
    private final DestinationRepository destinationRepository;
    private final DestinationRepositoryExtension destinationRepositoryExtension;

    public Map<Integer, Integer> getRatingDistribution(String destinationId) {
        int[] distribution = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new CustomException(ErrorType.DESTINATION_NOT_FOUND))
                .getRatingCount();
        
        Map<Integer, Integer> resultMap = new TreeMap<>();
        for (int i = 0; i < 5; i++)
            resultMap.put(i+1, distribution[i]);
        return resultMap;
    }
    
    public void validateExists(String destinationId) {
        if (!destinationRepository.existsById(destinationId)) {
            throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
        }
    }

    public void increaseReviewCountAndRating(String destinationId, int rating) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new CustomException(ErrorType.DESTINATION_NOT_FOUND));
        float approximateAverage = (float) (destination.getRatingSum() + rating) / (destination.getReviewCount() + 1);

        destinationRepositoryExtension.increaseReviewCountAndRating(destinationId, rating, approximateAverage);
    }

    public void decreaseReviewCountAndRating(String destinationId, Review review) {
        destinationRepositoryExtension.decreaseReviewCountAndRating(destinationId, review);
    }
    public boolean isApproved(String destinationId) {
        return destinationRepository.existsById(destinationId);
    }
}
