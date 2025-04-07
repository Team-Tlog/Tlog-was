package com.se.Tlog.domain.Travel.repository.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Travel.domain.Destination;


@Repository
public interface DestinationRepository extends MongoRepository<Destination, String> {

    boolean existsByName(String name);

    @Query("{'tags.isDeleted' :  false}")
    Page<Destination> findAllWithActiveTags(Pageable pageable);

}
