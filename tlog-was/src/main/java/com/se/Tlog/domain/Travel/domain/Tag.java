package com.se.Tlog.domain.Travel.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
	@Id
	private String id;
	private String name;

	private boolean isDeleted = false;

	private Tag(String name, int weight) {
		this.name = name;
	}

	public static Tag createTag(String name, int weight) {
        return new Tag(name, weight);
	}

	public void updateTagDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
