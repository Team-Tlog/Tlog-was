package com.se.Tlog.domain.Travel.repository;

import com.se.Tlog.domain.Travel.Entity.Destination;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends MongoRepository<Destination, String> {
    List<Destination> findByTagsIdContains(String tagId);

    boolean existsByName(String name);
}
