package com.se.Tlog.domain.Wishlist.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistOwnerDto;
import com.se.Tlog.domain.Wishlist.repository.mongo.WishlistRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Component
@RequiredArgsConstructor
public class WishlistService {
	private final WishlistRepository wishlistRepository;
	private final DestinationRepository destinationRepository;
	private final WishlistServiceValidator serviceValidator;
	
	/**
	 * 위시리스트(스크랩/장바구니 등) 항목을 반환합니다.
	 * @param ownerDto
	 * @param wishlistType
	 * @return
	 */
	public List<Destination> getWishlistData(WishlistOwnerDto ownerDto, WishlistType wishlistType) {
		serviceValidator.validateOwner(ownerDto);
		serviceValidator.validateWishlistType(ownerDto.ownerType(), wishlistType);
		
		Optional<Wishlist> wishlist = wishlistRepository.
				findByOwnerIdAndOwnerType(ownerDto.ownerId(), ownerDto.ownerType());
		if (wishlist.isPresent())
			return destinationRepository.findAllById(wishlist.get().getWishlistItems(wishlistType));
		else
			return new ArrayList<Destination>();
	}
	
	/**
	 * 각종 위시리스트(스크랩/장바구니 등)의 여행지를 추가/제거합니다.
	 * @param updateType
	 * @param ownerDto
	 * @param wishlistType
	 * @param destinationId
	 */
	public void updateWishlist(
			UpdateType updateType,
			WishlistOwnerDto ownerDto, 
			WishlistType wishlistType, 
			String destinationId) {
		serviceValidator.validateOwner(ownerDto);
		serviceValidator.validateWishlistType(ownerDto.ownerType(), wishlistType);
		if (UpdateType.ADD == updateType)
			serviceValidator.validateDestination(destinationId);

		Wishlist wishlist = wishlistRepository.
				findByOwnerIdAndOwnerType(ownerDto.ownerId(), ownerDto.ownerType())
				.orElseGet(() -> Wishlist.create(ownerDto.ownerType(), ownerDto.ownerId()));
		
		if (UpdateType.ADD == updateType)
			wishlist.addDestination(destinationId, wishlistType);
		if (UpdateType.DELETE == updateType)
			wishlist.deleteDestination(destinationId, wishlistType);
		wishlistRepository.save(wishlist);
	}
}
