package io.security.corespringsecurity.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.security.corespringsecurity.domain.AccountContext;
import io.security.corespringsecurity.security.token.AjaxAuthenticationToken;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	private UserDetailsService detailsService;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		AccountContext accountContext = (AccountContext) detailsService.loadUserByUsername(username);

		if(!encoder.matches(password, accountContext.getAccount().getPassword())) {
			throw new BadCredentialsException("BadCredentialsException");
		}

		AjaxAuthenticationToken authenticationToken = new AjaxAuthenticationToken(accountContext.getAccount(), null,  accountContext.getAuthorities());
		return authenticationToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(AjaxAuthenticationToken.class);
	}

}
