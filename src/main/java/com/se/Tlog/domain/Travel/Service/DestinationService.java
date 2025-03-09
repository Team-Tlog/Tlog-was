package com.se.Tlog.domain.Travel.Service;

import com.se.Tlog.domain.Travel.Entity.Destination;
import com.se.Tlog.domain.Travel.Entity.Tag;
import com.se.Tlog.domain.Travel.Entity.TagInfo;
import com.se.Tlog.domain.Travel.dto.DestinationDto;
import com.se.Tlog.domain.Travel.repository.DestinationRepository;
import com.se.Tlog.domain.Travel.repository.TagRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final TagRepository tagRepository;

    public void createDestination(DestinationDto destinationDto) {
        if(destinationRepository.existsByName(destinationDto.getName()))
            throw new CustomException(ErrorType.ALREADY_EXISTS_DESTINATION);

        List<TagInfo> tagInfoList = destinationDto.getTags().stream()
                .map(tagIdDto -> {
                    Tag existingTag = tagRepository.findById(tagIdDto.getTagId())
                            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_TAG));
                    return new TagInfo(existingTag.getId(), tagIdDto.getWeight());
                })
                .toList();


        Destination destination = DestinationDto.toEntity(destinationDto,tagInfoList);
        destinationRepository.save(destination);
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Destination getDestinationById(String id) {
        return destinationRepository.findById(id).orElseThrow(() -> new RuntimeException("Destination not found"));
    }
}
