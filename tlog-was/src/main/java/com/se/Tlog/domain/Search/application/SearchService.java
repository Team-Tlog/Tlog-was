package com.se.Tlog.domain.Search.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Search.repository.mongo.DestinationSearchRepository;
import com.se.Tlog.domain.Travel.application.DestinationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SearchService {
	private final DestinationSearchRepository searchRepository;
	private final DestinationService destinationService;
    
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
}
