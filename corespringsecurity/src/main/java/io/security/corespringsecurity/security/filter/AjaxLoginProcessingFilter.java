package io.security.corespringsecurity.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.security.corespringsecurity.domain.AccountDto;
import io.security.corespringsecurity.security.token.AjaxAuthenticationToken;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private ObjectMapper objectMapper = new ObjectMapper();

	public AjaxLoginProcessingFilter() {
		super(new AntPathRequestMatcher("/api/login"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		if(!isAjax(request)) {
			throw new IllegalStateException("Authentication is not supported");
		}

		AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);

		if(isEmptyAccount(accountDto)){
			throw new IllegalStateException("Username or password is empty");
		}

		AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());
		return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
	}

	private boolean isEmptyAccount(AccountDto accountDto) {
		return StringUtils.isEmpty(accountDto.getUsername()) || StringUtils.isEmpty(accountDto.getPassword());
	}

	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-with"));
	}

}
