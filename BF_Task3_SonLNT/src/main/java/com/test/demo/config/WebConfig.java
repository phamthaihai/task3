package com.test.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.test.demo.interceptor.CurrentUserInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final CurrentUserInterceptor currentUserInterceptor;

	public WebConfig(CurrentUserInterceptor currentUserInterceptor) {
		this.currentUserInterceptor = currentUserInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(currentUserInterceptor)
				.addPathPatterns("/home", "/about", "/contact", "/profile/**", "/login-history/**", "/todo/**");
	}

}
