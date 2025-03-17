package com.se.Tlog.domain.User.application;

import com.se.Tlog.domain.User.infrastructure.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        }
    }
}
