package com.se.Tlog.domain.Travel.domain.repository;

import com.se.Tlog.domain.Travel.controller.dto.DestinationSimilarDto;
import com.se.Tlog.domain.Travel.domain.TagCount;

import java.util.List;

public interface CustomTagRepositoryService {
    void addCustomTag(String destinationId, List<String> tagNameList);
    List<DestinationSimilarDto> findRelatedDestinations(String destinationId, List<TagCount> topTags);
}
