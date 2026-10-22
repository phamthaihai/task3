package com.test.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class TodoForm {

	@NotBlank(message = "Please describe the task")
	@Size(max = 200, message = "Task must be at most 200 characters")
	private String title;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dueDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

}
