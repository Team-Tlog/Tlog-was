package com.se.Tlog.domain.Social.Post.repository.mongo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Social.Post.domain.Post;

@Repository
interface RawPostQueryRepository extends MongoRepository<Post, String> {
    @Aggregation(pipeline = {
            "{ $sort: { _id: -1 } }",
            "{ $match: { $expr: { $lt: [ '$_id', ObjectId(?1) ] } } }",
            "{ $match: { author: { $in: ?2 } } }",
            "{ $limit: ?0 }"
    })
    List<Post> findAllFollowersRecentPosts(int size, String lastPostId, List<UUID> followingList);
}
