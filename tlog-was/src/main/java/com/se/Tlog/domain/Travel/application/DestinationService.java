package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.TagInfo;
import com.se.Tlog.domain.Travel.infrastructure.DestinationRepositoryServiceImplement;
import com.se.Tlog.domain.Travel.infrastructure.TagRepositoryServiceImplement;
import com.se.Tlog.domain.Travel.infrastructure.mongo.DestinationRepository;
import com.se.Tlog.domain.Travel.presentation.dto.DestinationDto;

import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepositoryServiceImplement destinationRepoService;
    private final DestinationRepository destinationRepository;
    private final TagRepositoryServiceImplement tagRepoService;

    public void createDestination(DestinationDto destinationDto) {
    	Destination.assertValidity(destinationDto.getName(), destinationRepoService);
        List<TagInfo> tagInfoList = destinationDto.getTags().stream()
                .map(tagIdDto -> TagInfo.create(tagIdDto.getTagId(), tagIdDto.getWeight(), tagRepoService))
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

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Destination getDestinationById(String id) {
        return destinationRepository.findById(id).orElseThrow(() -> new RuntimeException("Destination not found"));
    }
}
