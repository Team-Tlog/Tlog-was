package com.se.Tlog.domain.User.application;


import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final AccessTokenProvider accessTokenProvider;
    private final UserRepository userRepository;

    /*@Transactional
    public void updateEmail(UUID userId, String email) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
        user.updateEmail(email);
    }*/

    @Transactional
    public String updateSnsId(UUID userId, String snsId) {
        if (userRepository.existsBySnsId(snsId)) {
            throw new CustomException(ErrorType.ALREADY_EXISTS_SNSId);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        user.updateSnsId(snsId);
        return accessTokenProvider.generateToken(
                user.getId().toString(),
                user.getRole().getValue(),
                user.getSnsId()
        );
    }
}
