package com.se.Tlog.domain.Travel.repository;

import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.TagInfo;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Travel.domain.repository.DestinationRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DestinationRepositoryServiceImplement implements DestinationRepositoryService {
    private final DestinationRepository destinationRepository;

    @Override
    public boolean exist(String name) {
        return destinationRepository.existsByName(name);
    }

    @Override
    public void addFixedTags(String id, List<TagInfo> fixedTags) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorType.DESTINATION_NOT_FOUND));

        destination.addFixedTags(fixedTags);
		destinationRepository.save(destination);
    }
}
