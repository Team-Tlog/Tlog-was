package com.se.Tlog.domain.Social.Post.repository.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Social.Post.domain.Post;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostRepositoryExtension {
    private final PostRepository postRepository;
    
    private static final String NULL_LAST_POST_ID = "ffffffffffffffffffffffff";
    
    public Slice<Post> findAllRecentPosts(int size, String lastPostId, List<UUID> followingList) {
        if (size <= 0) size = 10;
        if (lastPostId == null || !ObjectId.isValid(lastPostId)) lastPostId = NULL_LAST_POST_ID;
        if (followingList == null) followingList = new ArrayList<UUID>();
        
        List<Post> results = new ArrayList<Post>(
                postRepository.findAllFollowersRecentPosts(size + 1, lastPostId, followingList));
        boolean hasNext = (size + 1 == results.size());
        if (hasNext) results.remove(size);
        return new SliceImpl<Post>(results, PageRequest.ofSize(size), hasNext);
    }
}
