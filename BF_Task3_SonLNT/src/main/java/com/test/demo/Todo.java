package com.test.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todo_tasks")
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 320)
	private String userEmail;

	@Column(nullable = false, length = 200)
	private String title;

	private LocalDate dueDate;

	@Column(nullable = false)
	private boolean completed;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	protected Todo() {
	}

	public Todo(String userEmail, String title, LocalDate dueDate) {
		this.userEmail = userEmail;
		this.title = title;
		this.dueDate = dueDate;
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

}
