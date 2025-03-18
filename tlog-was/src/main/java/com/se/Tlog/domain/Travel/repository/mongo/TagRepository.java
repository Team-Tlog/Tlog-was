package com.se.Tlog.domain.Travel.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Travel.domain.Tag;

import java.util.Optional;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    Optional<Tag> findByName(String name);

    boolean existsByName(String name);
}
