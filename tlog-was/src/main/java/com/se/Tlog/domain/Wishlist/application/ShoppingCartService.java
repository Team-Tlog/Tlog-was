package com.se.Tlog.domain.Wishlist.application;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationRes;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;
import com.se.Tlog.domain.Wishlist.domain.WishlistService;
import com.se.Tlog.domain.Wishlist.domain.WishlistType;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistOwnerDto;
import com.se.Tlog.domain.Wishlist.domain.UpdateType;

@ApplicationService
public class ShoppingCartService {
	@Autowired
	WishlistService wishlistService;

	public List<DestinationRes> getCartData(UUID ownerId, OwnerType ownerType) {
		return wishlistService.getWishlistData(
				new WishlistOwnerDto(
						ownerType,
						ownerId),
				WishlistType.SHOPPING_CART)
				.stream().map(DestinationRes::from).toList();
	}
	
	private void updateCart(UpdateType updateType, UUID ownerId, OwnerType ownerType, String destinationId) {
		wishlistService.updateWishlist(
				updateType,
				new WishlistOwnerDto(
						ownerType,
						ownerId),
				WishlistType.SHOPPING_CART, 
				destinationId);
	}
	
	public void addToCart(UUID ownerId, OwnerType ownerType, String destinationId) {
		updateCart(UpdateType.ADD, ownerId, ownerType, destinationId);
	}
	
	public void deleteFromCart(UUID ownerId, OwnerType ownerType, String destinationId) {
		updateCart(UpdateType.DELETE, ownerId, ownerType, destinationId);
	}
}
