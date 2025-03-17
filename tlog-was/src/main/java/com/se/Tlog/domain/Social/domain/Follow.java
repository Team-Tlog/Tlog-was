package com.se.Tlog.domain.Social.domain;



import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(
        name = "follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fromUserId", "toUserId"})
)
@NoArgsConstructor(access = PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID fromUserId;
    private UUID toUserId;


    LocalDateTime followedAt = LocalDateTime.now();

    private Follow(UUID fromUserId, UUID toUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

    public static Follow follow(UUID fromUserId, UUID toUserId) {
        return new Follow(fromUserId, toUserId);
    }
}
