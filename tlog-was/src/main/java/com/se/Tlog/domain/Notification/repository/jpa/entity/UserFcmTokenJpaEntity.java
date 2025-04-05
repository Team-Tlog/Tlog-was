package com.se.Tlog.domain.Notification.repository.jpa.entity;

import java.util.UUID;

import com.se.Tlog.domain.User.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userfcmtoken")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFcmTokenJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private String fcmToken;
	
	public UserFcmTokenJpaEntity(User user, String fcmToken) {
		this.user = user;
		this.fcmToken = fcmToken;
	}
	
	public void updateFirebaseToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}
}
