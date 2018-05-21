package kubernetes.client.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import kubernetes.client.config.MyLogger;
import kubernetes.client.security.AuthenticatedUser;

public abstract class BaseController {
	protected final MyLogger logger = MyLogger.getLogger(getClass());

	@ModelAttribute("authenticatedUser")
	public AuthenticatedUser authenticatedUser(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		return authenticatedUser;
	}

	public static AuthenticatedUser getCurrentUser() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof AuthenticatedUser) {
			return ((AuthenticatedUser) principal);
		}
		// principal object is either null or represents anonymous user -
		// neither of which our domain User object can represent - so return null
		return null;
	}

	public static boolean isLoggedIn() {
		return getCurrentUser() != null;
	}

}
