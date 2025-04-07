package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDto;
import com.se.Tlog.domain.Travel.controller.dto.DestinationRes;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.TagInfo;
import com.se.Tlog.domain.Travel.domain.repository.DestinationRepositoryService;
import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;

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

    public void createDestination(DestinationDto destinationDto) {
    	Destination.assertValidity(destinationDto.getName(), destinationRepoService);
        List<TagInfo> tagInfoList = destinationDto.getTags().stream()
                .map(tagIdDto -> TagInfo.create(tagIdDto.tagId(), tagIdDto.weight(),tagRepoService))
                .toList();
        Destination destination = Destination.create(
        		destinationDto.getName(),
                destinationDto.getLocation(),
                destinationDto.getRating(),
                destinationDto.getAddress(),
                tagInfoList,
                destinationDto.getCity(),
                destinationDto.isHasParking(),
                destinationDto.isPetFriendly(),
                destinationRepoService);
        destinationRepository.save(destination);
    }

    public Page<DestinationRes> getAllDestinations(Pageable pageable) {
        Page<Destination> destinationPage = destinationRepository.findAllWithActiveTags(pageable);
        List<DestinationRes> destinationResList = destinationPage
                .stream()
                .map(DestinationRes::from).toList();

        return new PageImpl<>(destinationResList, pageable, destinationPage.getTotalElements());
    }

    public DestinationRes getDestinationById(String id) {
        Destination destination = destinationRepository.findById(id).orElseThrow(() -> new RuntimeException("Destination not found"));

        return DestinationRes.from(destination);
    }
}
