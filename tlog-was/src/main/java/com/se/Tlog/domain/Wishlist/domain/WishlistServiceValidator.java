package com.se.Tlog.domain.Wishlist.domain;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistOwnerDto;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Component
@RequiredArgsConstructor
public class WishlistServiceValidator {
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final DestinationRepository destinationRepository;

	public void validateOwner(WishlistOwnerDto ownerDto) {
		switch (ownerDto.ownerType()) {
		case USER:
			if (!userRepository.existsById(ownerDto.ownerId()))
				throw new CustomException(ErrorType.USER_NOT_FOUND);
			return;
		case TEAM:
			if (!teamRepository.existsById(ownerDto.ownerId()))
				throw new CustomException(ErrorType.TEAM_NOT_FOUND);
			return;
		default:
			log.error("장바구니 주인 타입" + ownerDto.ownerType().toString() +"에 대한 처리가 구현되지 않았습니다!");
			throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void validateWishlistType(OwnerType ownerType, WishlistType listType) {
		if (OwnerType.TEAM == ownerType
			&& WishlistType.SCRAP == listType)
			throw new CustomException(ErrorType.SCRAP_UNSUPPORTED_TO_TEAM);
	}
	
	public void validateDestination(String destinationId) {
		if (!destinationRepository.existsById(destinationId))
			throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
	}
}
