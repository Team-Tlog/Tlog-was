package com.se.Tlog.domain.Wishlist.domain.dto;


import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.Location;
import com.se.Tlog.domain.Travel.domain.TagCount;

import java.util.List;

public record WishlistDestinationRes(
        String id,
        String name,
        Location location,
        String imageUrl,
        String description,
        List<TagCount> tagCountList
) {
    public static WishlistDestinationRes from(Destination destination, List<TagCount> topTags) {
        return new WishlistDestinationRes(
                destination.getId(),
                destination.getName(),
                destination.getLocation(),
                destination.getImageUrl(),
                destination.getDescription(),
                topTags
        );
    }
}
