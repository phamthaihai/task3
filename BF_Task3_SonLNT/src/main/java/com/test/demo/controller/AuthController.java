package com.test.demo.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.demo.dto.LoginForm;
import com.test.demo.dto.SignupForm;
import com.test.demo.entity.Users;
import com.test.demo.exception.DuplicateEmailException;
import com.test.demo.service.UserService;

@Controller
public class AuthController {

	public static final String SESSION_USER_ID = "userId";

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/")
	public String index(HttpSession session) {
		return session.getAttribute(SESSION_USER_ID) != null ? "redirect:/home" : "redirect:/login";
	}

	@GetMapping("/signup")
	public String signupPage(@ModelAttribute SignupForm signupForm) {
		return "signup";
	}

	@PostMapping("/signup")
	public String signup(@Valid @ModelAttribute SignupForm signupForm, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("error", firstError(result, "name", "email", "password"));
			signupForm.setPassword(null);
			return "signup";
		}
		try {
			userService.register(signupForm.getName(), signupForm.getEmail(), signupForm.getPassword());
		}
		catch (DuplicateEmailException ex) {
			model.addAttribute("error", ex.getMessage());
			signupForm.setPassword(null);
			return "signup";
		}
		redirectAttributes.addFlashAttribute("registered", true);
		redirectAttributes.addFlashAttribute("email", signupForm.getEmail().toLowerCase());
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String loginPage(@ModelAttribute LoginForm loginForm, Model model, HttpSession session) {
		if (session.getAttribute(SESSION_USER_ID) != null && model.getAttribute("loggedIn") == null) {
			return "redirect:/home";
		}
		if (model.getAttribute("email") instanceof String email) {
			loginForm.setEmail(email);
		}
		return "login";
	}

	@PostMapping("/login")
	public String login(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("error", firstError(result, "email", "password"));
			loginForm.setPassword(null);
			return "login";
		}
		Optional<Users> user = userService.authenticate(loginForm.getEmail(), loginForm.getPassword());
		if (user.isEmpty()) {
			model.addAttribute("error", "Invalid email or password");
			loginForm.setPassword(null);
			return "login";
		}
		session.setAttribute(SESSION_USER_ID, user.get().getId());
		redirectAttributes.addFlashAttribute("loggedIn", true);
		redirectAttributes.addFlashAttribute("email", user.get().getEmail());
		return "redirect:/login";
	}

	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

	private static String firstError(BindingResult result, String... fieldOrder) {
		for (String field : fieldOrder) {
			FieldError error = result.getFieldError(field);
			if (error != null) {
				return error.getDefaultMessage();
			}
		}
		return "Please check the form and try again";
	}

}
