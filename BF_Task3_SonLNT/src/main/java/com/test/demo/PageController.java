package com.test.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

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
