package com.se.Tlog.domain.User.domain;

import static jakarta.persistence.FetchType.LAZY;

import java.util.List;
import java.util.UUID;

import com.se.Tlog.domain.Travel.domain.Tag;

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
}
