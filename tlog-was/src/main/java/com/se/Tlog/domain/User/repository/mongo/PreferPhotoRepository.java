package com.se.Tlog.domain.User.repository.mongo;

import com.se.Tlog.domain.User.domain.PreferPhoto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PreferPhotoRepository extends MongoRepository<PreferPhoto, Integer> {
}
