package io.security.corespringsecurity.security.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import lombok.Getter;

@Getter
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {


	private String secretKey;

	public FormWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		secretKey = request.getParameter("secret_key");
	}

}
