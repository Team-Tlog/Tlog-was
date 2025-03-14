package com.se.Tlog.domain.Travel.domain;

import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TagInfo {
    private String id;
    private int weight;
    
    public static TagInfo create(String tagId, int tagWeight, TagRepositoryService validator) {
    	if (!validator.existById(tagId))
    		new CustomException(ErrorType.NOT_FOUND_TAG);
    	return new TagInfo(tagId, tagWeight);
    }
}
