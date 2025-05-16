package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Review.controller.dto.DestinationReviewDto;
import com.se.Tlog.domain.Review.domain.service.ReviewDomainService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDetailsRes;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDto;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Travel.domain.TagInfo;
import com.se.Tlog.domain.Travel.domain.repository.DestinationRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepositoryService destinationRepoService;
    private final DestinationRepository destinationRepository;
    private final CustomTagService customTagService;
    private final ReviewDomainService reviewDomainService;
    private final RedisUtil redisUtil;

    public void createDestination(DestinationDto destinationDto) {
    	Destination.assertValidity(destinationDto.getName(), destinationRepoService);

        List<String> customTags = destinationDto.getCustomTags();

        Destination destination = Destination.create(
        		destinationDto.getName(),
                destinationDto.getLocation(),
                destinationDto.getAddress(),
                new ArrayList<>(),
                destinationDto.getCity(),
                destinationDto.getDistrict(),
                destinationDto.isHasParking(),
                destinationDto.isPetFriendly(),
                destinationDto.getDescription(),
                destinationDto.getImageUrl(),
                destinationRepoService);
        String destinationId = destinationRepository.save(destination).getId();
        customTagService.addCustomTag(destinationId, customTags);
        redisUtil.pushDestinationIdToTaggingQueue(destinationId);
    }

    public Page<DestinationSummaryRes> getAllDestinations(Pageable pageable) {
        return convertToDto(destinationRepository.findAllWithActiveTags(pageable));
    }
    
    private Page<DestinationSummaryRes> convertToDto(Page<Destination> destinationPage) {
        Map<String, List<TagCount>> tagCountMap = customTagService.getAllTopTags(
                destinationPage.map(Destination::getId).toList(),
                3);

        List<DestinationSummaryRes> destinationResList = destinationPage
                .stream()
                .map(destination -> DestinationSummaryRes.from(
                        destination,
                        tagCountMap.get(destination.getId())))
                .toList();

        return new PageImpl<>(destinationResList, destinationPage.getPageable(), destinationPage.getTotalElements());
    }

    public DestinationDetailsRes getDestinationById(String id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorType.DESTINATION_NOT_FOUND));

        List<TagCount> topTags = customTagService.getTopTags(destination.getId(), 3);
        List<DestinationReviewDto> top2Reviews = reviewDomainService.getTop2Reviews(destination.getId());

        return DestinationDetailsRes.from(destination, topTags, top2Reviews);
    }

    public void addFixedTagsToDestination(String destinationId,List<TagInfo> fixedTags) {
        destinationRepoService.addFixedTags(destinationId, fixedTags);
    }
}
