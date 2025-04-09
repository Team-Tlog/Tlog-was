package com.se.Tlog.domain.Wishlist.repository.mongo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.se.Tlog.domain.Wishlist.domain.Wishlist;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {
	Optional<Wishlist> findByOwnerIdAndOwnerType(UUID ownerId, OwnerType ownerType);
}
