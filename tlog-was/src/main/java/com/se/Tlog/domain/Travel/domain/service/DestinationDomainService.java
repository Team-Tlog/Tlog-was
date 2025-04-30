package com.se.Tlog.domain.Travel.domain.service;

import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestinationDomainService {
    private final DestinationRepository destinationRepository;


    public void validateExists(String destinationId) {
        if (!destinationRepository.existsById(destinationId)) {
            throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
        }
    }
}
