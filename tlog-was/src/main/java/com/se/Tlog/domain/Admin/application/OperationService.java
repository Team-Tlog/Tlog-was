package com.se.Tlog.domain.Admin.application;

import com.se.Tlog.domain.Admin.controller.dto.CreateFeedbackReq;
import com.se.Tlog.domain.Admin.domain.Feedback;
import com.se.Tlog.domain.Admin.repository.mongo.FeedbackRepository;
import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class OperationService {
    private final FeedbackRepository feedbackRepository;

    public void makeFeedback(UUID userId, CreateFeedbackReq request) {
        if (userId == null)
            throw new CustomException(ErrorType.UN_AUTHORIZATION);
        if (request == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);

        Feedback newFeedback = Feedback.create(
                userId,
                request.title(),
                request.content(),
                request.refImageUrls());
        feedbackRepository.save(newFeedback);
    }
}
