package com.se.Tlog.domain.Search.repository.mongo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Travel.domain.Destination;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DestinationSearchRepository {
    private final RawDestinationSearchRepository rawRepository;
    
    public List<Destination> autoCompleteByAddress(String address) {
        return rawRepository.autoCompleteByAddress(address);
	}
	
    public List<Destination> autoCompleteByName(String name) {
	    return rawRepository.autoCompleteByName(name);
	}

    public Page<Destination> searchByCity(String city, Pageable pageable) {
        RawPagedResultDto result = rawRepository.searchByCity(city, pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<Destination>(
                result.pagedDestinations(), 
                pageable, 
                result.totalSize());
    }
    
    public Page<Destination> searchByNameAndCity(String name, String city, Pageable pageable) {
        RawPagedResultDto result = rawRepository.searchByNameAndCity(name, city, pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<Destination>(
                result.pagedDestinations(), 
                pageable, 
                result.totalSize());
    }
}
