package com.test.demo.controller;

import java.io.IOException;
import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.demo.dto.ChangePasswordForm;
import com.test.demo.entity.UserPhoto;
import com.test.demo.entity.Users;
import com.test.demo.interceptor.CurrentUserInterceptor;
import com.test.demo.repository.UserPhotoRepository;
import com.test.demo.service.UserService;

@Controller
public class ProfileController {

	private static final long MAX_PHOTO_BYTES = 2 * 1024 * 1024;
	private static final Set<String> PHOTO_TYPES = Set.of("image/png", "image/jpeg", "image/gif", "image/webp");

	private final UserService userService;
	private final UserPhotoRepository userPhotoRepository;

	public ProfileController(UserService userService, UserPhotoRepository userPhotoRepository) {
		this.userService = userService;
		this.userPhotoRepository = userPhotoRepository;
	}

	@GetMapping("/profile")
	public String profile(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user,
			@ModelAttribute ChangePasswordForm changePasswordForm, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("hasPhoto", userPhotoRepository.existsById(user.getId()));
		return "profile";
	}

	@PostMapping("/profile/password")
	public String changePassword(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user,
			@Valid @ModelAttribute ChangePasswordForm changePasswordForm, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		String error = null;
		if (result.hasErrors()) {
			error = firstError(result, "currentPassword", "newPassword", "confirmPassword");
		}
		else if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())) {
			error = "New passwords do not match";
		}
		else if (!userService.changePassword(user, changePasswordForm.getCurrentPassword(),
				changePasswordForm.getNewPassword())) {
			error = "Current password is incorrect";
		}
		if (error != null) {
			model.addAttribute("passwordError", error);
			return profile(user, new ChangePasswordForm(), model);
		}
		redirectAttributes.addFlashAttribute("passwordSuccess", "Your password has been updated");
		return "redirect:/profile";
	}

	@PostMapping("/profile/photo")
	public String uploadPhoto(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user,
			@RequestParam("photo") MultipartFile photo, RedirectAttributes redirectAttributes) throws IOException {
		if (photo.isEmpty()) {
			redirectAttributes.addFlashAttribute("photoError", "Please choose an image to upload");
		}
		else if (!PHOTO_TYPES.contains(photo.getContentType())) {
			redirectAttributes.addFlashAttribute("photoError", "Photo must be a PNG, JPEG, GIF or WebP image");
		}
		else if (photo.getSize() > MAX_PHOTO_BYTES) {
			redirectAttributes.addFlashAttribute("photoError", "Photo must be 2 MB or smaller");
		}
		else {
			userPhotoRepository.save(new UserPhoto(user.getId(), photo.getBytes(), photo.getContentType()));
			redirectAttributes.addFlashAttribute("photoSuccess", "Profile photo updated");
		}
		return "redirect:/profile";
	}

	@GetMapping("/profile/photo")
	public ResponseEntity<byte[]> photo(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user) {
		return userPhotoRepository.findById(user.getId())
				.map(photo -> ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(photo.getContentType()))
						.cacheControl(CacheControl.noCache().cachePrivate())
						.header("X-Content-Type-Options", "nosniff")
						.body(photo.getData()))
				.orElseGet(() -> ResponseEntity.notFound().build());
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
