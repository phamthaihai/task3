package com.test.demo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupForm {

	@NotBlank(message = "Name is required")
	@Size(max = 255, message = "Name must be at most 255 characters")
	private String name;

	@NotBlank(message = "Email address is required")
	@Email(message = "Please enter a valid email address")
	@Size(max = 320, message = "Email address is too long")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

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
