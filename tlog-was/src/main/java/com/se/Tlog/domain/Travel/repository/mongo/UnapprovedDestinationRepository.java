package com.se.Tlog.domain.Travel.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Travel.domain.UnapprovedDestination;

@Repository
public interface UnapprovedDestinationRepository extends MongoRepository<UnapprovedDestination, String>{
}
