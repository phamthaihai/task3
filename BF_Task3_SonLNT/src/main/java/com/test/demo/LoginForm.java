package com.test.demo;

import jakarta.validation.constraints.NotBlank;

public class LoginForm {

	@NotBlank(message = "Email address is required")
	private String email;

	@NotBlank(message = "Password is required")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
