package com.se.Tlog.domain.Wishlist.application;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.application.CustomTagService;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;
import com.se.Tlog.domain.Wishlist.domain.WishlistService;
import com.se.Tlog.domain.Wishlist.domain.WishlistType;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistDestinationRes;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistOwnerDto;

import lombok.RequiredArgsConstructor;

import com.se.Tlog.domain.Wishlist.domain.UpdateType;

@ApplicationService
@RequiredArgsConstructor
public class ShoppingCartService {
	private final WishlistService wishlistService;
	private final CustomTagService customTagService;

	public List<WishlistDestinationRes> getCartData(UUID ownerId, OwnerType ownerType) {
	    List<Destination> destinations = 
                wishlistService.getWishlistData(
                        new WishlistOwnerDto(
                                ownerType,
                                ownerId),
                        WishlistType.SHOPPING_CART);
        Map<String, List<TagCount>> tagCountMap = customTagService.getAllTopTags(
                destinations.stream().map(Destination::getId).toList(),
                3);
        
        return destinations.stream()
                .map(destination ->  WishlistDestinationRes.from(
                        destination, 
                        tagCountMap.get(destination.getId())))
                .toList();
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
