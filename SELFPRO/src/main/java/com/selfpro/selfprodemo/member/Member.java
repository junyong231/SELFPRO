package com.selfpro.selfprodemo.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스
	private Long memberId;

	private String memberLoginID;

	private String memberLoginPW;

	@Column(nullable = false)
	@Min(0)
	private int dailyEXP;

	@Column(nullable = false)
	@Min(1)
	private int memberLevel = 1;

	@Column(nullable = false)
	private boolean todayClosingDone = false;
}


