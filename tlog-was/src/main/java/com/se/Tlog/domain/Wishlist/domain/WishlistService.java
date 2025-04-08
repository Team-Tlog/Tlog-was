package com.se.Tlog.domain.Wishlist.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistOwnerDto;
import com.se.Tlog.domain.Wishlist.repository.mongo.WishlistRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Component
@RequiredArgsConstructor
public class WishlistService {
	private final WishlistRepository wishlistRepository;
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final DestinationRepository destinationRepository;
	
	private void validateOwner(WishlistOwnerDto ownerDto, WishlistType listType) {
		if (OwnerType.USER == ownerDto.ownerType()) {
			if (!userRepository.existsById(ownerDto.ownerId()))
				throw new CustomException(ErrorType.USER_NOT_FOUND);
		}
		else if (OwnerType.TEAM == ownerDto.ownerType()) {
			if (!teamRepository.existsById(ownerDto.ownerId()))
				throw new CustomException(ErrorType.TEAM_NOT_FOUND);
			if (WishlistType.SCRAP == listType)
				throw new CustomException(ErrorType.SCRAP_UNSUPPORTED_TO_TEAM);
		}
		else {
			log.error("장바구니 주인 타입" + ownerDto.ownerType().toString() +"에 대한 처리가 구현되지 않았습니다!");
			throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 위시리스트(스크랩/장바구니 등) 항목을 반환합니다.
	 * @param ownerDto
	 * @param wishlistType
	 * @return
	 */
	public List<Destination> getWishlistData(WishlistOwnerDto ownerDto, WishlistType wishlistType) {
		validateOwner(ownerDto, wishlistType);
		
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
		validateOwner(ownerDto, wishlistType);
		if (UpdateType.ADD == updateType &&
				!destinationRepository.existsById(destinationId))
			throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);

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
