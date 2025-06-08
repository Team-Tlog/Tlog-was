package com.se.Tlog.domain.Social.Post.repository.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Social.Post.domain.Reply;

@Repository
public interface ReplyRepository extends MongoRepository<Reply, String> {
    static final String NULL_LAST_REPLY_ID = "000000000000000000000000";
    static final String NULL_PARENT_ID = "000000000000000000000000";
    
    @Aggregation(pipeline = {
            // id 기반 페이징 (-> 성능상 좀 더 우수)
            """
            { $sort: { _id: 1 } }
            """,
            // 결과 쿼리
            """
            {
                $match: {
                    _id: { $gt: ObjectId(?1) },
                    postId: ?2
                    parentId: "000000000000000000000000"
                }
            }
            """,
            """
            { $limit: ?0 }
            """
    })
    List<Reply> findAllByPostId(int limit, String lastReplyId, String postId);
    
    @Aggregation(pipeline = {
            // 결과 쿼리
            """
            {
                $match: {
                    postId: { $in: ?1 },
                    parentId: "000000000000000000000000"
                }
            }
            """,
            """
            { $limit: ?0 }
            """
    })
    List<Reply> findAllByPostIds(int limit, Set<String> postId);
    
    @Aggregation(pipeline = {
            // id 기반 페이징 (-> 성능상 좀 더 우수)
            """
            { $sort: { _id: 1 } }
            """,
            // 결과 쿼리
            """
            {
                $match: {
                    _id: { $gt: ObjectId(?1) },
                    parentId: ?2
                }
            }
            """,
            """
            { $limit: ?0 }
            """,
            
    })
    List<Reply> findAllByParentId(int limit, String lastReplyId, String parentId);
    
    default Map<String, List<Reply>> findAllOfPosts(int size, Set<String> postIds) {
        List<Reply> replies = findAllByPostIds(size, postIds);
        Map<String, List<Reply>> result = new HashMap<String, List<Reply>>();
        for (Reply r : replies) {
            if (result.containsKey(r.getPostId()))
                result.get(r.getPostId()).add(r);
            else {
                List<Reply> newList = new ArrayList<Reply>();
                newList.add(r);
                result.put(r.getPostId(), newList);
            }
        }
        return result;
    }

    default List<Reply> findAllOfPost(int size, String lastRelyId, String postId) {
        return findAllByPostId(
                size,
                (null == lastRelyId ? NULL_LAST_REPLY_ID : lastRelyId),
                postId);
    }
    
    default List<Reply> findAllOfReply(int size, String lastRelyId, String parentReplyId) {
        return findAllByParentId(
                size,
                (null == lastRelyId ? NULL_LAST_REPLY_ID : lastRelyId),
                parentReplyId);
    }
}
