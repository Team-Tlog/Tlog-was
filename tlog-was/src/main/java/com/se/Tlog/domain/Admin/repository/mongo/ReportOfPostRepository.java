package com.se.Tlog.domain.Admin.repository.mongo;

import com.se.Tlog.domain.Admin.domain.ReportOfPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportOfPostRepository extends MongoRepository<ReportOfPost, String> {
    ReportOfPost findByPostId(String postId);
}
