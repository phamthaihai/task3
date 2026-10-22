package com.test.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** A user's profile photo, kept apart from {@link Users} so the image bytes are only loaded when served. */
@Entity
@Table(name = "user_photos")
public class UserPhoto {

	@Id
	private Long userId;

	@Column(nullable = false, columnDefinition = "LONGBLOB")
	private byte[] data;

	@Column(nullable = false, length = 50)
	private String contentType;

	protected UserPhoto() {
	}

	public UserPhoto(Long userId, byte[] data, String contentType) {
		this.userId = userId;
		this.data = data;
		this.contentType = contentType;
	}

	public Long getUserId() {
		return userId;
	}

	public byte[] getData() {
		return data;
	}

	public String getContentType() {
		return contentType;
	}

}
