package com.se.Tlog.domain.Search.application;

import java.util.List;
import java.util.stream.Collectors;

import com.se.Tlog.domain.Search.controller.dto.PopularDestinationDto;
import com.se.Tlog.domain.Search.repository.mongo.PopularityRepository;
import com.se.Tlog.domain.Travel.domain.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Search.repository.mongo.DestinationSearchRepository;
import com.se.Tlog.domain.Travel.application.DestinationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class DescriptionSearchService {
	private final DestinationSearchRepository searchRepository;
	private final DestinationService destinationService;
	private final PopularityRepository popularityRepository;
    
    public List<DestinationSummaryRes> autoCompleteDestinationByAddress(String address) {
        return destinationService.convertToDto(
                searchRepository.autoCompleteByAddress(address));
    }
	
	public List<DestinationSummaryRes> autoCompleteDestinationByName(String name) {
		return destinationService.convertToDto(
		        searchRepository.autoCompleteByName(name));
	}
	
	public Page<DestinationSummaryRes> searchDestinationByCity(Pageable pageable, String city) {
	    return destinationService.convertToDto(
	            searchRepository.searchByCity(city, pageable));
    }
    
    public Page<DestinationSummaryRes> searchDestinationByCityAndName(Pageable pageable, String city, String name) {
        return destinationService.convertToDto(
                searchRepository.searchByNameAndCity(name, city, pageable));
    }

	public List<PopularDestinationDto> getPopularDestinations() {List<Destination> popDests = popularityRepository.findPopularDestinations(10);
		List<PopularDestinationDto> result = popDests.stream()
				.skip(1)
				.map(PopularDestinationDto::create)
				.collect(Collectors.toList());
		if (!popDests.isEmpty())
			result.add(0, PopularDestinationDto.create(popDests.get(0), "전국"));
		return result;
	}
}
