package com.se.Tlog.domain.Travel.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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
	

	@Builder
	public Destination(String name, Location location, int rating, String address,
					   List<TagInfo> tags, boolean verified,String city, boolean hasParking,
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
}
