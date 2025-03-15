package com.se.Tlog.domain.Travel.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.mongodb.core.mapping.Document;

import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

@Document(collection = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
	@Id
	private String id;
	private String name;

	private Tag(String name, int weight) {
		this.name = name;
	}

	public static Tag createTag(String name, int weight, TagRepositoryService validator) {
        if(validator.existByName(name))
            throw new CustomException(ErrorType.ALREADY_EXISTS_TAG);
        return new Tag(name, weight);
	}
}
