package com.test.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import com.test.demo.entity.Users;
import com.test.demo.interceptor.CurrentUserInterceptor;
import com.test.demo.repository.EventRepository;

@Controller
public class EventController {

	private final EventRepository eventRepository;

	public EventController(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	@GetMapping("/event")
	public String events(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("events", eventRepository.findAllByOrderByEventDateAscStartTimeAsc());
		return "event";
	}

}
