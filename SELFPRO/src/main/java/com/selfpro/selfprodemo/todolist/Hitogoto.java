package com.selfpro.selfprodemo.todolist;

import java.time.LocalDate;

import com.selfpro.selfprodemo.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hitogoto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스
	private Long hitogotoId;

	@ManyToOne // 여러 Hitogoto가 하나의 Member에 속함
    @JoinColumn(name = "memberId", nullable = false)
    private Member member; // Member 객체 자체를 필드로 가집니다.

	private LocalDate writeDate;

	private String content;
}
