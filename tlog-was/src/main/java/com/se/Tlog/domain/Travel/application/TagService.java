package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.controller.dto.TagDto;
import com.se.Tlog.domain.Travel.domain.Tag;
import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.TagRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagRepositoryService tagRepo;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public void createTag(TagDto tagDto) {
    	Tag newTag = Tag.createTag(tagDto.getName(), 0, tagRepo);
        tagRepository.save(newTag);
    }
}
