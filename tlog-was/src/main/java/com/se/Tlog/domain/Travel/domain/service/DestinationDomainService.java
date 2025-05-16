package com.se.Tlog.domain.Travel.domain.service;

import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.redis.RedisUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestinationDomainService {
    private final DestinationRepository destinationRepository;
    private final RedisUtil redisUtil;


    public void validateExists(String destinationId) {
        if (!destinationRepository.existsById(destinationId)) {
            throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
        }
    }

    public boolean isApproved(String destinationId) {
        return redisUtil.isInTaggingQueue(destinationId);
    }
}
