package com.est.jobshin.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String errorMessage = "로그인 실패";

		if (exception instanceof BadCredentialsException) {
			errorMessage = "비밀번호를 잘못 입력 했습니다.";
		} else if (exception instanceof UsernameNotFoundException) {
			errorMessage = "사용자를 찾을 수 없습니다.";
		}

		String requestURI = request.getRequestURI();

		if (requestURI.contains("/user/login")) {
			getRedirectStrategy().sendRedirect(request, response,
					"/user/login?error=" + errorMessage);
		}
	}
}