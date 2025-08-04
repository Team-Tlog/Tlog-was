package com.se.Tlog.domain.Admin.repository.mongo;

import com.se.Tlog.domain.Admin.domain.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
}
