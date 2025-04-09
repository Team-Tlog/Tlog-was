package com.se.Tlog.domain.Wishlist.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Document(collection = "wishlists")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist {
	@Id
	private String id;
	
	private UUID ownerId;
	private OwnerType ownerType;
	
	private List<String> scrapList = new ArrayList<String>();
	private List<String> shoppingCart = new ArrayList<String>();

	private Wishlist(OwnerType ownerType, UUID ownerId) {
		this.ownerType = ownerType;
		this.ownerId = ownerId;
	}
	
	public static Wishlist create(OwnerType ownerType, UUID ownerId) {
		return new Wishlist(ownerType, ownerId);
	}
	
	public List<String> getWishlistItems(WishlistType listType) {
		switch (listType) {
		case SCRAP:
			return new ArrayList<String>(scrapList);
		case SHOPPING_CART:
			return new ArrayList<String>(shoppingCart);
		default:
			log.error("장바구니 타입" + listType.toString() +"에 대한 처리가 구현되지 않았습니다!");
			return new ArrayList<String>();
		}
	}

	public void addDestination(String destinationId, WishlistType listType) {
		switch (listType) {
		case SCRAP:
			if (!scrapList.contains(destinationId))
				scrapList.add(destinationId);
			return;
		case SHOPPING_CART:
			if (!shoppingCart.contains(destinationId))
				shoppingCart.add(destinationId);
			return;
		default:
			log.error("장바구니 타입" + listType.toString() +"에 대한 처리가 구현되지 않았습니다!");
		}
	}
	
	public void deleteDestination(String destinationId, WishlistType listType) {
		switch (listType) {
		case SCRAP:
			if (!scrapList.contains(destinationId))
				throw new CustomException(ErrorType.SCRAP_NOT_FOUND);
			scrapList.remove(destinationId);
			return;
		case SHOPPING_CART:
			if (!shoppingCart.contains(destinationId))
				throw new CustomException(ErrorType.SHOPCART_NOT_FOUND);
			shoppingCart.remove(destinationId);
			return;
		default:
			log.error("장바구니 타입" + listType.toString() +"에 대한 처리가 구현되지 않았습니다!");
		}
	}
}
