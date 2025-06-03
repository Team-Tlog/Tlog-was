package com.se.Tlog.domain.Travel.domain;

import java.util.ArrayList;
import java.util.List;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
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
	private String district;

	private Location location;

	private List<TagInfo> tags = new ArrayList<>();

	private boolean hasParking;
	private boolean petFriendly;

	private String imageUrl;

	private int ratingSum;
	private int reviewCount;
	private float averageRating;

	private int[] ratingCount = new int[5];

	private String description;

	public static void assertValidity(String name, DestinationRepositoryService destinationRepo) {
		if (destinationRepo.exist(name))
			throw new CustomException(ErrorType.ALREADY_EXISTS_DESTINATION);
	}
	
	public static Destination create(
			String name, 
			Location location,
			String address,
			List<TagInfo> tags,
			String city,
			String district,
			boolean hasParking,
			boolean petFriendly,
			String imageUrl,
			String description,
			DestinationRepositoryService validator) {
		assertValidity(name, validator);
		return new Destination(name, location,address, tags, false, city, district, hasParking, petFriendly, imageUrl, description);
	}

	private Destination(
			String name, 
			Location location,
			String address,
			List<TagInfo> tags, 
			boolean verified,
			String city,
			String district,
			boolean hasParking,
			boolean petFriendly,
			String imageUrl,
			String description) {
		this.name = name;
		this.location = location;
		this.address = address;
		this.tags = tags;
		this.city = city;
		this.district = district;
		this.hasParking = hasParking;
		this.petFriendly = petFriendly;
		this.imageUrl = imageUrl;
		this.description = description;
		this.ratingSum = 0;
		this.reviewCount = 0;
		this.averageRating = 0;
	}

	public void addTag(TagInfo tag) {
		this.tags.add(tag);
	}

	public void addFixedTags(List<TagInfo> fixedTags) {
		this.tags.addAll(fixedTags);
	}
}
