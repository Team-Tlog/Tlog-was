package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDetailsRes;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDto;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Travel.domain.TagInfo;
import com.se.Tlog.domain.Travel.domain.repository.DestinationRepositoryService;
import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepositoryService destinationRepoService;
    private final DestinationRepository destinationRepository;
    private final TagRepositoryService tagRepoService;
    private final CustomTagService customTagService;

    public void createDestination(DestinationDto destinationDto) {
    	Destination.assertValidity(destinationDto.getName(), destinationRepoService);
        List<TagInfo> tagInfoList = destinationDto.getTags().stream()
                .map(tagIdDto -> TagInfo.create(tagIdDto.tagId(), tagIdDto.weight(),tagRepoService))
                .toList();
        Destination destination = Destination.create(
        		destinationDto.getName(),
                destinationDto.getLocation(),
                destinationDto.getAddress(),
                tagInfoList,
                destinationDto.getCity(),
                destinationDto.getDistrict(),
                destinationDto.isHasParking(),
                destinationDto.isPetFriendly(),
                destinationDto.getDescription(),
                destinationDto.getImageUrl(),
                destinationRepoService);
        destinationRepository.save(destination);
    }

    public Page<DestinationSummaryRes> getAllDestinations(Pageable pageable) {
        Page<Destination> destinationPage = destinationRepository.findAllWithActiveTags(pageable);

        List<DestinationSummaryRes> destinationResList = destinationPage
                .stream()
                .map(destination -> {
                            List<TagCount> topTags = customTagService.getTopTags(destination.getId(), 3);
                            return DestinationSummaryRes.from(destination, topTags);
                }).toList();

        return new PageImpl<>(destinationResList, pageable, destinationPage.getTotalElements());
    }

    public DestinationDetailsRes getDestinationById(String id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorType.DESTINATION_NOT_FOUND));

        List<TagCount> topTags = customTagService.getTopTags(destination.getId(), 3);
        return DestinationDetailsRes.from(destination, topTags);
    }
}
