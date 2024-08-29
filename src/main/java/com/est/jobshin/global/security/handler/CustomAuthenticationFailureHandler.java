package com.est.jobshin.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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

		String encodedErrorMessage = URLEncoder.encode(errorMessage, "UTF-8");
		String requestURI = request.getRequestURI();

		// 로그인 페이지로 리다이렉트하며, 에러 메시지를 전달합니다.
		if (requestURI.contains("/views/users/login")) {
			getRedirectStrategy().sendRedirect(request, response, "/views/users/login?error=" + encodedErrorMessage);
		}
	}
}