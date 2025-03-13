package com.se.Tlog.domain.Travel.Entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
	@Id
	private String id;
	private String name;

	@Builder
	public Tag(String name, int weight) {
		this.name = name;
	}

	public static void createTag(String name,int weight) {
		Tag tag = Tag.builder()
				.name(name)
				.build();
	}
}
