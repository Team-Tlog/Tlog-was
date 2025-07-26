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
import com.se.Tlog.domain.User.repository.jpa.UserRepositoryExtension;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostRepositoryExtension {
    private final PostRepository postRepository;
    private final UserRepositoryExtension userRepositoryExtension;
    
    private static final String NULL_LAST_POST_ID = "ffffffffffffffffffffffff";
    
    private Slice<Post> queryPostsFromUserIds(int size, String lastPostId, List<UUID> userIds) {
        if (0 == userIds.size())
            return new SliceImpl<Post>(new ArrayList<>(), PageRequest.ofSize(0), false);
        
        List<Post> results = new ArrayList<Post>(
                postRepository.findAllRecentPostsByUsers(size + 1, lastPostId, userIds));
        boolean hasNext = (size + 1 == results.size());
        if (hasNext) results.remove(size);
        return new SliceImpl<Post>(results, PageRequest.ofSize(size), hasNext);
    }
    
    public Slice<Post> findAllFollowersRecentPosts(int size, String lastPostId, List<UUID> followingList) {
        if (size <= 0) size = 10;
        if (lastPostId == null || !ObjectId.isValid(lastPostId)) lastPostId = NULL_LAST_POST_ID;
        if (followingList == null) followingList = new ArrayList<UUID>();

        return queryPostsFromUserIds(size, lastPostId, followingList);
    }
    
    public Slice<Post> findAllRecentSuggestedPosts(int size, String lastPostId, UUID userId) {
        if (size <= 0) size = 10;
        if (lastPostId == null || !ObjectId.isValid(lastPostId)) lastPostId = NULL_LAST_POST_ID;
        
        // 유사한 TBTI를 가진 모든 유저를 조회 -> 해당 조건으로 게시글 필터링
        // 비효율적이며 성능에 문제가 우려됨 : 추후 게시글 자체에 TBTI 정보를 포함하는 등의 개선안 고려 필요
        
        List<UUID> relativeUsers = userRepositoryExtension.findAllIdsByRelativeTbti(userId);
        return queryPostsFromUserIds(size, lastPostId, relativeUsers);
    }
}
