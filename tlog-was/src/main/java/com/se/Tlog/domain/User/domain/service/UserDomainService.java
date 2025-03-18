package com.se.Tlog.domain.User.domain.service;


import com.se.Tlog.domain.User.controller.dto.UserSummaryDto;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepository userRepository;

    public List<UserSummaryDto> getUserByIds(List<UUID> uuidList) {
        return userRepository.findAllById(uuidList).stream()
                .map(UserSummaryDto::from)
                .toList();
    }

    public void validateExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        }
    }
}
