package com.se.Tlog.domain.Team.repository.jpa.entity;

import java.util.UUID;

import com.se.Tlog.domain.Team.domain.Team;
import com.se.Tlog.domain.User.domain.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(
        name = "teamUsers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"team", "user"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamUserJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "team_id")
	@NonNull
	private Team team;
	
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	@NonNull
	private User user;
	
	@Builder
	public TeamUserJpaEntity(Team team, User user) {
		this.team = team;
		this.user = user;
	}
}
