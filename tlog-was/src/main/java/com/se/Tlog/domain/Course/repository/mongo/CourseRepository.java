package com.se.Tlog.domain.Course.repository.mongo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Course.domain.Course;
import com.se.Tlog.domain.Course.domain.OwnerType;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    List<Course> findAllByOwnerIdAndOwnerType(UUID ownerId, OwnerType ownerType);
}
