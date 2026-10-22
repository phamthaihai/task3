package com.test.demo.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import com.test.demo.entity.LoginHistory;
import com.test.demo.entity.Users;
import com.test.demo.interceptor.CurrentUserInterceptor;
import com.test.demo.repository.LoginHistoryRepository;
import com.test.demo.util.ExcelExport;

@Controller
public class LoginHistoryController {

	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final LoginHistoryRepository loginHistoryRepository;

	public LoginHistoryController(LoginHistoryRepository loginHistoryRepository) {
		this.loginHistoryRepository = loginHistoryRepository;
	}

	@GetMapping("/login-history")
	public String history(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user, Model model) {
		List<LoginHistory> entries = loginHistoryRepository.findByEmailOrderByLoginTimeDesc(user.getEmail());
		model.addAttribute("user", user);
		model.addAttribute("entries", entries);
		model.addAttribute("successCount", entries.stream().filter(LoginHistory::isSuccessful).count());
		model.addAttribute("timeFormat", TIME_FORMAT);
		return "login-history";
	}

	@GetMapping("/login-history/export")
	public ResponseEntity<byte[]> export(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user) {
		List<List<String>> rows = loginHistoryRepository.findByEmailOrderByLoginTimeDesc(user.getEmail()).stream()
				.map(entry -> List.of(user.getName(), user.getEmail(), entry.getLoginTime().format(TIME_FORMAT),
						entry.isSuccessful() ? "Success" : "Failed"))
				.toList();
		return ExcelExport.download("login-history.xlsx", "Login history",
				List.of("Name", "Email", "Login time", "Result"), rows);
	}

}
