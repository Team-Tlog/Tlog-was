package com.se.Tlog.domain.Review.domain.service;

import com.se.Tlog.domain.Review.controller.dto.DestinationReviewDto;
import com.se.Tlog.domain.Review.repository.mongo.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewDomainService {
    private final ReviewRepository reviewRepository;

    public List<DestinationReviewDto> getTop2Reviews(String destinationId) {
        Pageable sortedPageable = PageRequest.of(0, 2, Sort.by(Sort.Order.desc("rating")).and(Sort.by(Sort.Order.desc("createAt"))));

        return reviewRepository.findByDestinationId(destinationId, sortedPageable)
                .map(DestinationReviewDto::from)
                .getContent();
    }
}
