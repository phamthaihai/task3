package com.test.demo;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Guards signed-in pages: redirects to the login page when there is no valid session user, otherwise exposes
 * the user as the {@value #CURRENT_USER} request attribute.
 */
@Component
public class CurrentUserInterceptor implements HandlerInterceptor {

	static final String CURRENT_USER = "currentUser";

	private final UserService userService;

	public CurrentUserInterceptor(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		Optional<Users> user = Optional.ofNullable(session)
				.map(s -> (Long) s.getAttribute(AuthController.SESSION_USER_ID))
				.flatMap(userService::findById);
		if (user.isEmpty()) {
			if (session != null) {
				session.invalidate();
			}
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		request.setAttribute(CURRENT_USER, user.get());
		return true;
	}

}
