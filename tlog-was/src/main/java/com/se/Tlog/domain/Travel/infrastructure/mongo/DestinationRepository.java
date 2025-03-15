package com.se.Tlog.domain.Travel.infrastructure.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Travel.domain.Destination;

import java.util.List;

@Repository
public interface DestinationRepository extends MongoRepository<Destination, String> {
    List<Destination> findByTagsIdContains(String tagId);

    boolean existsByName(String name);
}
