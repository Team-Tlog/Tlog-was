package com.se.Tlog.domain.Search.application;

import java.util.List;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Search.repository.DestinationRawData;
import com.se.Tlog.domain.Search.repository.mongo.DestinationSearchRepository;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SearchService {
	private final DestinationSearchRepository searchRepository;
	
	public List<DestinationRawData> autoComplete(String searchText) {
		return searchRepository.autoCompleteBy(searchText);
	}
}
