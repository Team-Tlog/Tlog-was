package com.se.Tlog.domain.User.domain;

import static jakarta.persistence.FetchType.LAZY;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.se.Tlog.domain.Tbti.domain.Tbti;
import com.se.Tlog.domain.Tbti.domain.TraitCategory;
import com.se.Tlog.domain.Travel.domain.Tag;
import com.se.Tlog.domain.Travel.domain.TagType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "tagId"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTagInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;
    
    @NonNull
    private String tagId;

    private double weight;
    
    // 25.7.28
    //   특별히 복잡한 로직이 요구되지 않는 부분이라
    //   static으로 분리하지 않고 그대로 생성자를 사용하도록 구성했습니다.
    public UserTagInfo(User user, String tagId) {
        this.user = user;
        this.tagId = tagId;
    }
    
    public static List<UserTagInfo> of(User user, List<Tag> tags) {
        return tags.stream().map(tag -> new UserTagInfo(user, tag.getId())).toList();
    }

    public static Map<TagType, Double> getTagWeightMap(Tbti tbti, Map<TagType, Tag> allTags, Map<TagType, Long> preferTagCount) {
        return Arrays.stream(TagType.values())
                .filter(allTags::containsKey)
                .collect(Collectors.toMap(
                        Function.identity(),
                        type -> {
                            Tag tag = allTags.get(type);
                            long cnt = preferTagCount.getOrDefault(type, 0L);
                            return calculateWeight(tbti, type, cnt);
                        }
                ));
    }

    private static double calculateWeight(Tbti tbti, TagType type, long count) {
        double weight = 0.0;
        if (type == TagType.MOUNTAIN
         || type == TagType.OCEAN) {
            weight = 0.8 + (-0.4/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE);
        }
        if (type == TagType.CREEK) {
            weight += (0.4/99) * tbti.getPercentage(TraitCategory.RISK_TAKING)
                    + 0.2 + (-0.2/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL)
                    + 0.4 + (-0.6/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE);
        }
        if (type == TagType.PARK) {
            weight += 0.7 + (-0.2/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE);
        }
        if (type == TagType.RIVER
         || type == TagType.STREAM) {
            weight += 0.8 + (-0.3/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE);
        }
        if (type == TagType.GARDEN) {
            weight += 0.7 + (-0.3/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE)
                    - 0.1 + (0.2/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.URBAN) {
            weight += 0.2 + (0.4/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE)
                    + 0.05 + (0.15/99) * tbti.getPercentage(TraitCategory.RISK_TAKING)
                    + 0.2 + (-0.15/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.NIGHT_VIEW) {
            weight += 0.4 + (0.4/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE);
        }
        if (type == TagType.REST) {
            weight += 0.3 + (0.4/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL)
                    + 0.2 + (-0.2/99) * tbti.getPercentage(TraitCategory.RISK_TAKING);
        }
        if (type == TagType.DATE
         || type == TagType.HOT_PLACE
         || type == TagType.SPA) {
            weight += 0.5;
        }
        if (type == TagType.HISTORIC_CULTURAL) {
            weight += 0.3 + (0.5/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.MUSEUM) {
            weight += 0.4 + (0.4/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.ACTIVITY
         || type == TagType.WATER_SPORTS) {
            weight += 0.8 + (-0.5/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.VACATION_SPOT) {
            weight += 0.6 + (-0.4/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE)
                    + (0.3/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.A_WALK) {
            weight += 0.4
                    + 0.2 + (-0.2/99) * tbti.getPercentage(TraitCategory.RISK_TAKING)
                    + 0.2 + (-0.2/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE);
        }
        if (type == TagType.ROAD_TRIP) {
            weight += 0.5;
        }
        if (type == TagType.SERENE) {
            weight += 0.7 + (-0.4/99) * tbti.getPercentage(TraitCategory.RISK_TAKING)
                    - 0.2 + (0.4/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.GALLERY) {
            weight += 0.5 + (0.3/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.MARKET) {
            weight += 0.5;
        }
        if (type == TagType.AMUSEMENT_PARK) {
            weight += (0.7/99) * tbti.getPercentage(TraitCategory.RISK_TAKING)
                    + 0.25 + (-0.45/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.HANDS_ON_ACTIVITY) {
            weight += 0.7 + (-0.3/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL)
                    + (0.2/99) * tbti.getPercentage(TraitCategory.PLANNING_STYLE);
        }
        if (type == TagType.SHOPPING) {
            weight += (0.5/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE)
                    + 0.1 + (0.1/99) * tbti.getPercentage(TraitCategory.RISK_TAKING)
                    + 0.3 + (-0.25/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.EXOTIC) {
            weight += 0.5;
        }
        if (type == TagType.LANDSCAPE) {
            weight += 0.7 + (-0.1/99) * tbti.getPercentage(TraitCategory.LOCATION_PREFERENCE)
                    + (0.2/99) * tbti.getPercentage(TraitCategory.ACTIVITY_LEVEL);
        }
        if (type == TagType.SPRING
         || type == TagType.SUMMER
         || type == TagType.AUTUMN
         || type == TagType.WINTER) {
            weight += 0.5;
        }

        weight += (0.05 * count);
        return Math.max(Math.min(weight, 0.8), 0.2);
    }
}
