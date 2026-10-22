package com.test.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "login_history")
public class LoginHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 320)
	private String email;

	@Column(nullable = false)
	private LocalDateTime loginTime;

	@Column(nullable = false)
	private boolean successful;

	protected LoginHistory() {
	}

	public LoginHistory(String email, LocalDateTime loginTime, boolean successful) {
		this.email = email;
		this.loginTime = loginTime;
		this.successful = successful;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public boolean isSuccessful() {
		return successful;
	}

}
