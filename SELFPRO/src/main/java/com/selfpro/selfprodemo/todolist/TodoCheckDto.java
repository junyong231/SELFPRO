package com.selfpro.selfprodemo.todolist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoCheckDto {

	// PK와 불리언 받기
	private Long todoId;

	private String content;

	private boolean isCompleted;

}
