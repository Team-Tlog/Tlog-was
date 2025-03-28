package com.se.Tlog.domain.Team.repository.jpa.entity;

import java.util.UUID;

import com.se.Tlog.domain.Team.domain.Team;
import com.se.Tlog.domain.User.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
	
	@ManyToOne
	@JoinColumn(name = "team_id")
	@NonNull
	private Team team;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@NonNull
	private User user;
	
	@Builder
	public TeamUserJpaEntity(Team team, User user) {
		this.team = team;
		this.user = user;
	}
}
