package com.se.Tlog.domain.Travel.repository;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Travel.domain.repository.DestinationRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DestinationRepositoryServiceImplement implements DestinationRepositoryService {
	private final DestinationRepository destinationRepository;

	@Override
	public boolean exist(String name) {
		return destinationRepository.existsByName(name);
	}
}
