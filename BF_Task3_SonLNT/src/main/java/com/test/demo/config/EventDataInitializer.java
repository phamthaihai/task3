package com.test.demo.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.test.demo.entity.Event;
import com.test.demo.repository.EventRepository;

/**
 * Seeds the sample online events shown on the event page when the events table is empty.
 */
@Component
public class EventDataInitializer implements ApplicationRunner {

	private final EventRepository eventRepository;

	public EventDataInitializer(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (eventRepository.count() > 0) {
			return;
		}
		eventRepository.saveAll(List.of(
				event("Tech Innovation Summit", 10, 10, 0, 12, 0, "Online (Zoom)", "tech-summit"),
				event("Digital Marketing Workshop", 11, 14, 0, 16, 0, "Online (Zoom)", "marketing"),
				event("Leadership & Team Building", 12, 9, 0, 11, 0, "Online (Google Meet)", "leadership"),
				event("AI & Machine Learning Bootcamp", 14, 13, 0, 15, 0, "Online (Zoom)", "ai-bootcamp"),
				event("Entrepreneurship Talk", 15, 17, 0, 18, 30, "Online (Zoom)", "entrepreneur"),
				event("UI/UX Design Conference", 16, 19, 0, 21, 0, "Online (Google Meet)", "uiux")));
	}

	private static Event event(String title, int day, int startHour, int startMinute, int endHour, int endMinute,
			String venue, String imageSeed) {
		return new Event(title, LocalDate.of(2026, 12, day), LocalTime.of(startHour, startMinute),
				LocalTime.of(endHour, endMinute), venue, "https://picsum.photos/seed/" + imageSeed + "/600/300");
	}

}
