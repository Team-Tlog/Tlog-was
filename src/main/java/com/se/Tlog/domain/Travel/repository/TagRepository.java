package com.se.Tlog.domain.Travel.repository;

import com.se.Tlog.domain.Travel.Entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    Optional<Tag> findByName(String name);

    boolean existsByName(String name);
}
