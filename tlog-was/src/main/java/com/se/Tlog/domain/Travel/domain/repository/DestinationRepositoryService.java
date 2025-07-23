package com.se.Tlog.domain.Travel.domain.repository;

import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.DestinationSortType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DestinationRepositoryService {
	void increaseReviewCountAndRating(String destinationId, int rating, float approximateAverage);
	void decreaseReviewCountAndRating(String destinationId, Review review);
	List<Destination> getDestinations(Pageable pageable, String city, DestinationSortType sortType);
}
