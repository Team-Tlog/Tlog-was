package com.se.Tlog.domain.Travel.repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Travel.domain.Tag;
import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.TagRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagRepositoryServiceImplement implements TagRepositoryService {
	private final TagRepository tagRepository;

    @Override
    public Set<String> getExistSet(List<String> tagIds) {
        return tagRepository.findAllById(tagIds).stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
    }
}
