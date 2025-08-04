package com.se.Tlog.domain.Admin.application;

import com.se.Tlog.domain.Admin.controller.dto.CreateFeedbackReq;
import com.se.Tlog.domain.Admin.controller.dto.CreateReportToPostReq;
import com.se.Tlog.domain.Admin.domain.Feedback;
import com.se.Tlog.domain.Admin.domain.ReportOfPost;
import com.se.Tlog.domain.Admin.repository.mongo.FeedbackRepository;
import com.se.Tlog.domain.Admin.repository.mongo.ReportOfPostRepository;
import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Social.Post.repository.mongo.PostRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class OperationService {
    private final FeedbackRepository feedbackRepository;
    private final PostRepository postRepository;
    private final ReportOfPostRepository reportOfPostRepository;

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

    public void makeReportToPost(UUID reporterId, CreateReportToPostReq request) {
        if (reporterId == null)
            throw new CustomException(ErrorType.UN_AUTHORIZATION);
        if (request == null || request.postId() == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        if (!postRepository.existsById(request.postId()))
            throw new CustomException(ErrorType.POST_NOT_FOUND);

        ReportOfPost report = reportOfPostRepository.findByPostId(request.postId());
        if (report != null) report.addReport(reporterId);
        else report = ReportOfPost.create(request.postId(), reporterId);
        reportOfPostRepository.save(report);
    }
}
