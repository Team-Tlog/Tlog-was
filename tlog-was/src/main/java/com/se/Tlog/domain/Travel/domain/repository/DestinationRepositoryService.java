package com.se.Tlog.domain.Travel.domain.repository;

import com.se.Tlog.domain.Review.domain.Review;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.DestinationSortType;
import com.se.Tlog.domain.Travel.domain.TagInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DestinationRepositoryService {
	boolean exist(String name);
	void addFixedTags(String id, List<TagInfo> fixedTags);
	void increaseReviewCountAndRating(String destinationId, int rating, float approximateAverage);
	void decreaseReviewCountAndRating(String destinationId, Review review);
	List<Destination> getDestinations(Pageable pageable, String city, DestinationSortType sortType);
}
