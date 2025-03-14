package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.Travel.domain.Tag;
import com.se.Tlog.domain.Travel.infrastructure.mongo.TagRepository;
import com.se.Tlog.domain.Travel.presentation.dto.TagDto;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public void createTag(TagDto tagDto) {
        if(tagRepository.existsByName(tagDto.getName()))
            throw new CustomException(ErrorType.ALREADY_EXISTS_TAG);

        Tag newTag = TagDto.toEntity(tagDto);
        tagRepository.save(newTag);
    }
}
