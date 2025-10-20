package com.ocoelhogabriel.security_control_custom.infrastructure.utils.message;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write("{\"error\": \"Access denied!\"}");
	}
}
