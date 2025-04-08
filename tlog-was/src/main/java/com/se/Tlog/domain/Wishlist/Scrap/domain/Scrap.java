package com.se.Tlog.domain.Wishlist.Scrap.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "scraps")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Scrap {
	@Id
	private String id;
	
    private UUID userId;
	
	private List<String> destinationIds = new ArrayList<String>();
	
	public void addDestination(String destinationId) {
		if (destinationIds.contains(destinationId))
			throw new CustomException(ErrorType.ALREADY_SCRAPED_DESTINATION);
		destinationIds.add(destinationId);
	}
	
	public void deleteDestination(String destinationId) {
		destinationIds.remove(destinationId);
	}
	
	private Scrap(UUID ownerId) {
		this.userId = ownerId;
	}
	
	public static Scrap create(UUID ownerId) {
		return new Scrap(ownerId);
	}
}
