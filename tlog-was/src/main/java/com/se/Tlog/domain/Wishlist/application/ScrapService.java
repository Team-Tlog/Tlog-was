package com.se.Tlog.domain.Wishlist.application;

import java.util.List;
import java.util.UUID;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.application.CustomTagService;
import com.se.Tlog.domain.Travel.controller.dto.SimpleDestinationRes;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;
import com.se.Tlog.domain.Wishlist.domain.WishlistService;
import com.se.Tlog.domain.Wishlist.domain.WishlistType;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistOwnerDto;

import lombok.RequiredArgsConstructor;

import com.se.Tlog.domain.Wishlist.domain.UpdateType;

@ApplicationService
@RequiredArgsConstructor
public class ScrapService {
	private final WishlistService wishlistService;
	private final CustomTagService customTagService;
	
	public List<SimpleDestinationRes> getScrapData(UUID userId) {
		return wishlistService.getWishlistData(
				new WishlistOwnerDto(
						OwnerType.USER,
						userId),
				WishlistType.SCRAP)
				.stream().map(destination -> {
					List<TagCount> topTags = customTagService.getTopTags(destination.getId(), 3);
					return SimpleDestinationRes.from(destination, topTags);
				}).toList();
	}
	
	private void updateScrap(UpdateType updateType, UUID userId, String destinationId) {
		wishlistService.updateWishlist(
				updateType,
				new WishlistOwnerDto(
						OwnerType.USER,
						userId),
				WishlistType.SCRAP, 
				destinationId);
	}
	
	public void scrapDestination(UUID userId, String destinationId) {
		updateScrap(UpdateType.ADD, userId, destinationId);
	}
	
	public void unscrapDescription(UUID userId, String destinationId) {
		updateScrap(UpdateType.DELETE, userId, destinationId);
	}
}
