package com.test.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

	private static final DateTimeFormatter DATE_LABEL = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

	private static final DateTimeFormatter TIME_LABEL = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Nationalized
	@Column(nullable = false, length = 200)
	private String title;

	@Column(nullable = false)
	private LocalDate eventDate;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Nationalized
	@Column(nullable = false, length = 200)
	private String venue;

	@Column(length = 500)
	private String imageUrl;

	protected Event() {
	}

	public Event(String title, LocalDate eventDate, LocalTime startTime, LocalTime endTime, String venue,
			String imageUrl) {
		this.title = title;
		this.eventDate = eventDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.venue = venue;
		this.imageUrl = imageUrl;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getEventDate() {
		return eventDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public String getVenue() {
		return venue;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getDateLabel() {
		return eventDate.format(DATE_LABEL);
	}

	public String getTimeLabel() {
		return startTime.format(TIME_LABEL) + " - " + endTime.format(TIME_LABEL);
	}

}
