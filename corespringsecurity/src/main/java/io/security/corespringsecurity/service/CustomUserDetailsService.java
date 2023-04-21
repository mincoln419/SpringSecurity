package io.security.corespringsecurity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.security.corespringsecurity.domain.entity.Account;
import io.security.corespringsecurity.domain.entity.AccountContext;
import io.security.corespringsecurity.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Account account = userRepository.findByUsername(username);

		if(account == null) {
			throw new UsernameNotFoundException(username);
		}
		List<GrantedAuthority> roles = account.getUserRoles()
					.stream()
					.map(userRole -> userRole.getRoleName())
					.collect(Collectors.toSet())
					.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		AccountContext accountContext = new AccountContext(account, roles);

		return accountContext;
	}

}
