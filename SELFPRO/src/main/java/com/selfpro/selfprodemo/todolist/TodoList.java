package com.selfpro.selfprodemo.todolist;

import java.time.LocalDate;

import com.selfpro.selfprodemo.member.Member;

import jakarta.persistence.Entity;
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
public class TodoList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스
	private Long todoId;

	@ManyToOne // 여러 TodoList가 하나의 Member에 속함
    @JoinColumn(name = "memberId", nullable = false) 
    private Member member;

	private LocalDate writeDate;

	// 과제 내용
	private String content;

	// 과제 성공 여부 (채점)
	private boolean isCompleted = false;

}
