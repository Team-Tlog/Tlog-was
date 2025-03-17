package com.se.Tlog.domain.User.domain.service;


import com.se.Tlog.domain.User.infrastructure.jpa.UserRepository;
import com.se.Tlog.domain.User.presentation.dto.UserSummaryDto;
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
}
