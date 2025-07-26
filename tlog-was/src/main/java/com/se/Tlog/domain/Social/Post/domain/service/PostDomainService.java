package com.se.Tlog.domain.Social.Post.domain.service;

import com.se.Tlog.domain.Social.Post.domain.Post;
import com.se.Tlog.domain.Social.Post.repository.mongo.PostRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostDomainService {
    private final PostRepository postRepository;

    public Post findByIdOrThrow(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorType.POST_NOT_FOUND));
    }

    public void updateLikeCount(String postId, int redisCount) {
        Post post = findByIdOrThrow(postId);
        post.updateLikeCount(redisCount);
        postRepository.save(post);
    }
}
