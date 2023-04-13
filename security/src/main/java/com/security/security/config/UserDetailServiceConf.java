package com.security.security.config;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailServiceConf {
	
//	@Bean 
//	public UserDetailsService userDetailsService(){
//		return username -> User.builder()
//				.username("mermer")
//				.password("1111")
//				.build();
//	}
	
}
