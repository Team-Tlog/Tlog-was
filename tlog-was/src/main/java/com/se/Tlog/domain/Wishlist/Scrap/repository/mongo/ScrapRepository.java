package com.se.Tlog.domain.Wishlist.Scrap.repository.mongo;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.se.Tlog.domain.Wishlist.Scrap.domain.Scrap;

public interface ScrapRepository extends MongoRepository<Scrap, String> {
	Scrap findByUserId(UUID userId);
}
