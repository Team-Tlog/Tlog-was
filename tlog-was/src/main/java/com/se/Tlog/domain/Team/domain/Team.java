package com.se.Tlog.domain.Team.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.se.Tlog.domain.Tbti.domain.Tbti;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private Long inviteCode;
	
	private String name;
	
	private Integer tbti;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	private Team(String name, Long inviteCode) {
		this.name = name;
		this.inviteCode = inviteCode;
		this.tbti = null;
	}
	
	static Team create(
			String name, 
			Long inviteCode) {
		if (name == null || name.isBlank())
			throw new CustomException(ErrorType.TEAM_NAME_NOT_FOUND);
		if (!InviteCodeUtil.isValidCode(inviteCode))
			throw new CustomException(ErrorType.INTERNAL_ERROR_BY_INVITE_CODE);
		
		Team newTeam = new Team(name, inviteCode);
		return newTeam;
	}
	
	public void setTbti(Tbti tbti) {
	    if (tbti == null)
	        this.tbti = null;
	    else
	        this.tbti = tbti.getTbtiCode();
	}
	
	public String getTbtiString() {
	    if (tbti == null)
	        return "아직 TBTI 검사가 진행되지 않았습니다!";
	    else 
	        return new Tbti(tbti).toString();
	}
}
