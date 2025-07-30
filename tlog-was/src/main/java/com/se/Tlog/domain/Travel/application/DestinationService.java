package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Review.controller.dto.DestinationReviewDto;
import com.se.Tlog.domain.Review.domain.service.ReviewDomainService;
import com.se.Tlog.domain.Travel.controller.dto.*;
import com.se.Tlog.domain.Travel.domain.*;
import com.se.Tlog.domain.Travel.repository.mongo.CustomTagRepositoryExtension;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepositoryExtension;
import com.se.Tlog.domain.Travel.repository.mongo.TagRepository;
import com.se.Tlog.domain.Travel.repository.mongo.UnapprovedDestinationRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

@ApplicationService
@RequiredArgsConstructor
public class DestinationService {
    private final CustomTagRepositoryExtension customTagRepositoryExtension;
    private final DestinationRepositoryExtension destinationRepoExtension;
    private final DestinationRepository destinationRepository;
    private final UnapprovedDestinationRepository unapprovedDestinationRepository;
    private final TagRepository tagRepository;
    
    private final CustomTagService customTagService;
    private final TagService tagService;
    private final ReviewDomainService reviewDomainService;

    public void generateNewDestination(DestinationDto destinationDto) {
        if (destinationRepository.existsByName(destinationDto.getName()))
            throw new CustomException(ErrorType.ALREADY_EXISTS_DESTINATION);
        
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
                destinationDto.getImageUrl());
        
        UnapprovedDestination newDestination = UnapprovedDestination.create(
                destinationDto.getCreater(), destinationData, destinationDto.getCustomTags());
        unapprovedDestinationRepository.save(newDestination);
    }
    
    public void assignDestination(String unapprovedDestinationId, List<AddFixedTagDto> fixedTags) {
        // 이 로직은 Travel 도메인보다도, admin에서만 활용되는 usecase임.. 구조 개선을 위해 고려해볼 것.
        UnapprovedDestination unapprovedDestination = unapprovedDestinationRepository.findById(unapprovedDestinationId)
                .orElseThrow(() -> new CustomException(ErrorType.ALREADY_APPROVED_DESTINATION));
        unapprovedDestinationRepository.deleteById(unapprovedDestinationId);
        
        Destination destination = unapprovedDestination.getDestination();
        destination.addFixedTags(tagService.createAll(fixedTags));
        destinationRepository.save(destination);
        customTagService.addCustomTag(destination.getId(), unapprovedDestination.getCustomTags());
    }

    public Slice<DestinationSummaryRes> getAllDestinations(Pageable pageable, String city,
                                                          DestinationSortType sortType, String tbti) {
        List<Destination> destinations = destinationRepoExtension.getDestinations(pageable, city, sortType);
        boolean hasNext = destinations.size() > pageable.getPageSize();

        if (hasNext) {
            destinations.remove(destinations.size() - 1);
        }

        List<DestinationSummaryRes> destinationSummaryRes = convertToDto(destinations);
        return new SliceImpl<>(new ArrayList<>(destinationSummaryRes), pageable, hasNext);
    }
    
    public Page<DestinationSummaryRes> getDestinationByIds(List<String> ids, Pageable pageable) {
        // 버그 위험성 : 순서가 보장되지 않을 경우 다음 페이지에 이전 페이지의 항목이 표시될 수 있습니다.
        // @Query(sort = "{'_id' : 1}") 가 추가될 것!
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

        List<DestinationSimilarDto> relatedDestinations = customTagRepositoryExtension.findRelatedDestinations(destination.getId(), topTags);
        return DestinationDetailsRes.from(destination, topTags, top2Reviews, relatedDestinations);
    }
    
    public List<DestinationByTagDto> getDestinationOfEachTag(int tbtiCode) {
        /* 25.7.28
         *   tbtiCode 값은
         *   나중에 TBTI 성향에 따라 태그 범위를 조정하는 기능에 사용될 예정입니다.
         *   ex) TBTI가 자연 성향 -> 산, 바다, 계곡과 같은 자연 태그를 더 많이 표시  
         */
        // Tbti tbti = new Tbti(tbtiCode);
        
        final int SAMPLE_SIZE = 10;
        
        // 전체 태그를 사용하지 않고 일부(최대 10개)만 무작위로 추출해 사용
        List<Tag> tags = tagRepository.findAll();
        List<Tag> sampleTags = tags;
        if (SAMPLE_SIZE < tags.size()) {
            Collections.shuffle(sampleTags, new Random(System.nanoTime()));
            sampleTags = sampleTags.subList(0, SAMPLE_SIZE);
        }
        
        Map<String, Destination> destinationMap = destinationRepoExtension.findAllByEachTags(
                sampleTags.stream().map(Tag::getId).toList());
        
        return sampleTags.stream()
                .filter(tag -> destinationMap.containsKey(tag.getId()))
                .map(tag -> {
                    Destination dest = destinationMap.get(tag.getId());
                    return new DestinationByTagDto(
                        dest.getName(),
                        dest.getImageUrl(),
                        tag.getName(), 
                        tag.getId());
                    })
                .toList();
    }
}
