package com.test.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.demo.entity.LoginHistory;
import com.test.demo.entity.Users;
import com.test.demo.exception.DuplicateEmailException;
import com.test.demo.repository.LoginHistoryRepository;
import com.test.demo.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final LoginHistoryRepository loginHistoryRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserService(UserRepository userRepository, LoginHistoryRepository loginHistoryRepository) {
		this.userRepository = userRepository;
		this.loginHistoryRepository = loginHistoryRepository;
	}

	public Users register(String name, String email, String password) {
		String normalizedEmail = normalizeEmail(email);
		if (userRepository.existsByEmail(normalizedEmail)) {
			throw new DuplicateEmailException();
		}
		Users user = new Users(name.trim(), normalizedEmail, passwordEncoder.encode(password));
		try {
			return userRepository.saveAndFlush(user);
		}
		catch (DataIntegrityViolationException ex) {
			// Another request registered the same email between the check and the insert.
			throw new DuplicateEmailException();
		}
	}

	public Optional<Users> findById(Long id) {
		return userRepository.findById(id);
	}

	/**
	 * Checks the credentials and records the attempt in the login history. Failed attempts are recorded only when
	 * the email belongs to an existing account, since there is no one to show them to otherwise.
	 */
	public Optional<Users> authenticate(String email, String password) {
		if (email == null || email.isBlank() || password == null || password.isBlank()) {
			return Optional.empty();
		}
		Optional<Users> account = userRepository.findByEmail(normalizeEmail(email));
		boolean success = account.filter(user -> passwordEncoder.matches(password, user.getPassword())).isPresent();
		account.ifPresent(user -> loginHistoryRepository.save(new LoginHistory(user.getEmail(), LocalDateTime.now(), success)));
		return success ? account : Optional.empty();
	}

	/** Returns false when {@code currentPassword} does not match the stored password. */
	public boolean changePassword(Users user, String currentPassword, String newPassword) {
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			return false;
		}
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		return true;
	}

	private static String normalizeEmail(String email) {
		return email.trim().toLowerCase();
	}

}
