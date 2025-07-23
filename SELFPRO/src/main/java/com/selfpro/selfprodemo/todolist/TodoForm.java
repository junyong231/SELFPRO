package com.selfpro.selfprodemo.todolist;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoForm {
	private List<TodoCheckDto> todos;
	private String hitogoto;
}
