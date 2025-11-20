package com.se.Tlog.domain.Course.repository.mongo;

import com.se.Tlog.domain.Course.domain.Course;
import com.se.Tlog.domain.Course.domain.OwnerType;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseMongoRepository extends MongoRepository<Course, String> {
    // MongoDB의 _id는 String이므로 <Course, String>

    /**
     * 소유자 ID (User 또는 Team)로 코스 목록을 조회합니다.
     */
    List<Course> findByOwnerId(UUID ownerId);
    List<Course> findByOwnerIdAndOwnerType(UUID ownerId, OwnerType ownerType, Sort sort);

}
