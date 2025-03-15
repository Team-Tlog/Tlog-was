package com.se.Tlog.domain.Travel.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.mongodb.core.mapping.Document;

import com.se.Tlog.domain.Travel.domain.repository.DestinationRepositoryService;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

@Document(collection = "destinations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Destination {
	@Id
	private String id;
	private String name;
	private String address;
	private String city;

	@Embedded
	private Location location;

	private List<TagInfo> tags = new ArrayList<>();

	private int rating;
	private boolean hasParking;
	private boolean petFriendly;
	
	public static void assertValidity(String name, DestinationRepositoryService destinationRepo) {
		if (destinationRepo.exist(name))
			throw new CustomException(ErrorType.ALREADY_EXISTS_DESTINATION);
	}
	
	public static Destination create(
			String name, 
			Location location, 
			int rating, 
			String address,
			List<TagInfo> tags,
			String city, 
			boolean hasParking,
			boolean petFriendly,
			DestinationRepositoryService validator) {
		assertValidity(name, validator);
		return new Destination(name, location, rating, address, tags, false, city, hasParking, petFriendly);
	}

	private Destination(
			String name, 
			Location location, 
			int rating, 
			String address,
			List<TagInfo> tags, 
			boolean verified,
			String city, 
			boolean hasParking,
			boolean petFriendly) {
		this.name = name;
		this.location = location;
		this.rating = rating;
		this.address = address;
		this.tags = tags;
		this.city = city;
		this.hasParking = hasParking;
		this.petFriendly = petFriendly;
	}

	public void addTag(TagInfo tag) {
		this.tags.add(tag);
	}
	
	public void verify() {
		// verified logic
	}
}
