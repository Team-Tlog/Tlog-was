package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Review.controller.dto.DestinationReviewDto;
import com.se.Tlog.domain.Review.domain.service.ReviewDomainService;
import com.se.Tlog.domain.Travel.controller.dto.*;
import com.se.Tlog.domain.Travel.domain.*;
import com.se.Tlog.domain.Travel.domain.repository.CustomTagRepositoryService;
import com.se.Tlog.domain.Travel.domain.repository.DestinationRepositoryService;
import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.domain.Travel.repository.mongo.UnapprovedDestinationRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepositoryService destinationRepoService;
    private final TagRepositoryService tagRepositoryService;
    private final DestinationRepository destinationRepository;
    private final UnapprovedDestinationRepository unapprovedDestinationRepository;
    private final CustomTagService customTagService;
    private final ReviewDomainService reviewDomainService;
    private final CustomTagRepositoryService customTagRepositoryService;

    public void generateNewDestination(DestinationDto destinationDto) {
        Destination destinationData = Destination.create(
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
        
        UnapprovedDestination newDestination = UnapprovedDestination.create(
                destinationDto.getCreater(), destinationData, destinationDto.getCustomTags());
        unapprovedDestinationRepository.save(newDestination);
    }
    
    public void assignDestination(String unapprovedDestinationId, List<AddFixedTagDto> fixedTags) {
        // 이 로직은 Travel 도메인보다도, admin에서만 활용되는 usecase임.. 구조 개선을 위해 고려해볼 것.
        UnapprovedDestination unapprovedDestination = unapprovedDestinationRepository.findById(unapprovedDestinationId)
                .orElseThrow(() -> new CustomException(ErrorType.ALREADY_APPROVED_DESTINATION));
        unapprovedDestinationRepository.deleteById(unapprovedDestinationId);
        
        String destinationId = destinationRepository.save(unapprovedDestination.getDestination()).getId();
        customTagService.addCustomTag(destinationId, unapprovedDestination.getCustomTags());
        destinationRepoService.addFixedTags(
                destinationId, 
                TagInfo.createAll(fixedTags, tagRepositoryService));
    }

    public Slice<DestinationSummaryRes> getAllDestinations(Pageable pageable, String city,
                                                          DestinationSortType sortType, String tbti) {
        List<Destination> destinations = destinationRepoService.getDestinations(pageable, city, sortType);
        boolean hasNext = destinations.size() > pageable.getPageSize();

        if (hasNext) {
            destinations.remove(destinations.size() - 1);
        }

        List<DestinationSummaryRes> destinationSummaryRes = convertToDto(destinations);
        return new SliceImpl<>(new ArrayList<>(destinationSummaryRes), pageable, hasNext);
    }
    
    public Page<DestinationSummaryRes> getDestinationByIds(List<String> ids, Pageable pageable) {
        return convertToDto(destinationRepository.findAllByIdIn(ids, pageable));
    }
    
    public List<DestinationSummaryRes> convertToDto(List<Destination> destinationList) {
        Map<String, List<TagCount>> tagCountMap = customTagService.getAllTopTags(
                destinationList.stream().map(Destination::getId).toList(),
                3);

        return destinationList
                .stream()
                .map(destination -> DestinationSummaryRes.from(
                        destination,
                        tagCountMap.get(destination.getId())))
                .toList();
    }
    
    public Page<DestinationSummaryRes> convertToDto(Page<Destination> destinationPage) {
        return new PageImpl<>(
                convertToDto(destinationPage.getContent()),
                destinationPage.getPageable(),
                destinationPage.getTotalElements());
    }

    public DestinationDetailsRes getDestinationById(String id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorType.DESTINATION_NOT_FOUND));

        List<TagCount> topTags = customTagService.getTopTags(destination.getId(), 3);
        List<DestinationReviewDto> top2Reviews = reviewDomainService.getTop2Reviews(destination.getId());

        List<DestinationSimilarDto> relatedDestinations = customTagRepositoryService.findRelatedDestinations(destination.getId(), topTags);
        return DestinationDetailsRes.from(destination, topTags, top2Reviews, relatedDestinations);
    }
}
