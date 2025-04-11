package com.se.Tlog.domain.Travel.repository.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Travel.domain.Tag;


@Repository
public interface TagRepository extends MongoRepository<Tag, String> {

    boolean existsByName(String name);

    @Query("{'isDeleted': false}")
    Page<Tag> findAllByActiveTags(Pageable pageable);
}
