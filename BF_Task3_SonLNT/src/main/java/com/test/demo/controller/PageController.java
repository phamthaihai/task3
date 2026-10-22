package com.test.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import com.test.demo.entity.Users;
import com.test.demo.interceptor.CurrentUserInterceptor;

@Controller
public class PageController {

	@GetMapping("/home")
	public String home(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user, Model model) {
		model.addAttribute("user", user);
		return "home";
	}

	@GetMapping("/about")
	public String about() {
		return "about";
	}

	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}

}
