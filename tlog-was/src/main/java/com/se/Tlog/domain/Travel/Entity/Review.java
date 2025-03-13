package com.se.Tlog.domain.Travel.Entity;


import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

	@Id
	private String id;

	private String userId;
	private String destinationId;

	private int rating;
	private String content;

	@Builder
	public Review(String userId, String destinationId, int rating, String content) {
		this.userId = userId;
		this.destinationId = destinationId;
		this.rating = rating;
		this.content = content;
	}
}