package com.se.Tlog.domain.Admin.application;

import com.se.Tlog.domain.Admin.controller.dto.CreateFeedbackReq;
import com.se.Tlog.domain.Admin.domain.Feedback;
import com.se.Tlog.domain.Admin.repository.mongo.FeedbackRepository;
import com.se.Tlog.domain.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class OperationService {
    private final FeedbackRepository feedbackRepository;

    public void makeFeedback(UUID userId, CreateFeedbackReq request) {
        Feedback newFeedback = Feedback.create(
                userId,
                request.title(),
                request.content(),
                request.refImageUrls());
        feedbackRepository.save(newFeedback);
    }
}
