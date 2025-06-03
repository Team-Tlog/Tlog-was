package com.se.Tlog.domain.Review.domain;


import com.se.Tlog.domain.Review.controller.dto.ReviewCreateDto;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Review {

	@Id
	private String id;

	private String userId;
	private String destinationId;

	// 추후 리팩토링 소요 : 사용자 정보를 리뷰에서 가지고 있음
	private String username;

	private int rating;
	private String content;
	private List<String> imageUrlList;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt; // 생성시간

	public static Review create(ReviewCreateDto dto) {
		return new Review(
				dto.userId(),
				dto.destinationId(),
				// 추후 리팩토링 소요 : 사용자 정보를 리뷰에서 가지고 있음
				dto.username(),
				dto.rating(),
				dto.content(),
				dto.imageUrlList()
		);
	}

	private Review(
			String userId,
			String destinationId,
			// 추후 리팩토링 소요 : 사용자 정보를 리뷰에서 가지고 있음
			String username,
			int rating,
			String content,
			List<String> imageUrlList
	){
		this.userId = userId;
		this.destinationId = destinationId;
		// 추후 리팩토링 소요 : 사용자 정보를 리뷰에서 가지고 있음
		this.username = username;
		this.rating = rating;
		this.content = content;
		this.imageUrlList = imageUrlList;
	}
}