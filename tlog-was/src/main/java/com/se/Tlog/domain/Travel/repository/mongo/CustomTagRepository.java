package com.se.Tlog.domain.Travel.repository.mongo;

import com.se.Tlog.domain.Travel.domain.CustomTagDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomTagRepository extends MongoRepository<CustomTagDocument,String> {
    Optional<CustomTagDocument> findByDestinationId(String destinationId);
    List<CustomTagDocument> findAllByDestinationIdIn(List<String> destinationIds);
}
