package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.controller.dto.TagDto;
import com.se.Tlog.domain.Travel.controller.dto.TagRes;
import com.se.Tlog.domain.Travel.domain.Tag;
import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.TagRepository;


import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepository;
    private final TagRepositoryService tagRepo;

    @Transactional(readOnly = true)
    public List<TagRes> getAllTags() {

        return tagRepository.findAll().stream()
                .map(tag -> TagRes.from(tag.getId(), tag.getName()))
                .toList();
    }

    public void createTag(TagDto tagDto) {
    	Tag newTag = Tag.createTag(tagDto.getName(), 0, tagRepo);
        tagRepository.save(newTag);
    }


    public void deleteTag(String tagId) {

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
        tag.markAsDeleted();
        tagRepository.save(tag);
    }
}
