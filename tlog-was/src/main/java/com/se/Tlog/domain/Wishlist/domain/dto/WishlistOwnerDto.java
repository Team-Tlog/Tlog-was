package com.se.Tlog.domain.Wishlist.domain.dto;

import java.util.UUID;

import com.se.Tlog.domain.Wishlist.domain.OwnerType;

public record WishlistOwnerDto(
		OwnerType ownerType,
		UUID ownerId) {

}
