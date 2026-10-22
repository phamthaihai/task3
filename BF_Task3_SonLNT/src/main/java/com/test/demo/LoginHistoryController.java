package com.test.demo;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class LoginHistoryController {

	static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
