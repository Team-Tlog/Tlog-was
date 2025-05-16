package com.se.Tlog.domain.Travel.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.se.Tlog.domain.Travel.controller.dto.AddFixedTagDto;
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
public class TagInfo { // 여행지가 갖고 있는 태그 정보
    private String id;
    private int weight;
    private boolean isDeleted;
    
    // 추후 CreateFixedTagDto로 입력받도록 변경 예정
    public static TagInfo create(String tagId, int tagWeight, TagRepositoryService validator) {
    	if (!validator.existById(tagId))
    		throw new CustomException(ErrorType.NOT_FOUND_TAG);
        return new TagInfo(tagId, tagWeight, false);
    }
    
    /**
     * 태그 생성 요청 중 유효한 요청에 대해서만 생성해 반환합니다.
     * <br>실패 상황 1. 주어진 id의 태그가 없을 경우
     * @param createTagRequests
     * @param tagRepositoryService
     * @return
     */
    public static List<TagInfo> createAll(List<AddFixedTagDto> createTagRequests, TagRepositoryService tagRepositoryService) {
        Set<String> validTagIds = tagRepositoryService.getExistSet(
                createTagRequests.stream().map(AddFixedTagDto::tagId).toList());
        
        return createTagRequests.stream()
            .filter(dto -> validTagIds.contains(dto.tagId()))
            .map(dto -> new TagInfo(dto.tagId(), dto.tagWeight(), false))
            .collect(Collectors.toList());
    }
}
