package com.test.demo.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.support.RequestContextUtils;

/** The multipart body is parsed before any controller runs, so an oversized upload has to be handled here. */
@ControllerAdvice
public class UploadErrorAdvice {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String photoTooLarge(HttpServletRequest request) {
		RequestContextUtils.getOutputFlashMap(request).put("photoError", "Photo must be 2 MB or smaller");
		return "redirect:/profile";
	}

}
