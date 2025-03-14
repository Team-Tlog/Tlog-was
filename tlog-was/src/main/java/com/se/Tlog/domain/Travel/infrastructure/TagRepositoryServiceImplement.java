package com.se.Tlog.domain.Travel.infrastructure;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.domain.Travel.infrastructure.mongo.TagRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagRepositoryServiceImplement implements TagRepositoryService {
	private final TagRepository tagRepository;
	
	@Override
	public boolean existById(String tagId) {
		return tagRepository.existsById(tagId);
	}

	@Override
	public boolean existByName(String tagName) {
		return tagRepository.existsByName(tagName);
	}
}
