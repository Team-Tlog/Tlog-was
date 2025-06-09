package com.se.Tlog.domain.Search.application;

import org.springframework.data.domain.Slice;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Search.repository.mongo.SnsPostSearchRepository;
import com.se.Tlog.domain.Social.Post.controller.dto.PostPreviewRes;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SnsPostSearchService {
    private final SnsPostSearchRepository postSearchRepository;
    
    public Slice<PostPreviewRes> searchOfDestinationAndContent(int size, String lastPostId, String query) {
        if (query == null || query.trim().length() <= 1)
            throw new CustomException(ErrorType.QUERY_TOO_SHORT);
        return postSearchRepository.searchOfDestinationsAndContent(size, lastPostId, query)
                .map(post -> {
                    if (0 < post.getImageUrls().size())
                        return new PostPreviewRes(post.getId(), post.getImageUrls().get(0));
                    else
                        return new PostPreviewRes(post.getId(), "");
                });
    }
}
