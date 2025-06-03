package com.se.Tlog.domain.User.domain.service;


import com.se.Tlog.domain.User.controller.dto.UserProfileInfo;
import com.se.Tlog.domain.User.controller.dto.UserSummaryDto;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;
import com.se.Tlog.domain.Wishlist.domain.WishlistService;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistOwnerDto;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepository userRepository;
    private final WishlistService wishlistService;

    public List<UserSummaryDto> getUserByIds(List<UUID> uuidList) {
        return userRepository.findAllById(uuidList).stream()
                .map(UserSummaryDto::from)
                .toList();
    }
    
    public Map<UUID,UserProfileInfo> getUserProfile(Set<UUID> userIds) {
        List<User> users = userRepository.findByIdIn(userIds);
        if(users.size() != userIds.size()){
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        }

        return users.stream()
                .collect(Collectors.toMap(User::getId, UserProfileInfo::of));
    }
    
    public Map<UUID,UserProfileInfo> getUserProfileOnlyExist(Set<UUID> userIds) {
        List<User> users = userRepository.findByIdIn(userIds);

        return users.stream()
                .collect(Collectors.toMap(User::getId, UserProfileInfo::of));
    }
    
    public void deleteUserAccount(UUID userId) {
    	validateExists(userId);
    	
    	// 각종 유저 계정 삭제 처리
    	wishlistService.deleteWishlist(new WishlistOwnerDto(OwnerType.USER, userId));
    	
    	userRepository.deleteById(userId);
    }

    public void validateExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        }
    }
}
