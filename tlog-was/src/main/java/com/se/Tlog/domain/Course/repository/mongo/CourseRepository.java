package com.se.Tlog.domain.Course.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Course.domain.Course;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
}
