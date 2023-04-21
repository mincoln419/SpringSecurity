package io.security.corespringsecurity.security.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.security.corespringsecurity.security.common.AjaxLoginAuthenticationEntryPoint;
import io.security.corespringsecurity.security.filter.AjaxLoginProcessingFilter;
import io.security.corespringsecurity.security.handler.AjaxAccessDeniedHandler;
import io.security.corespringsecurity.security.handler.AjaxFailureHandler;
import io.security.corespringsecurity.security.handler.AjaxSuccessHandler;
import io.security.corespringsecurity.security.provider.AjaxAuthenticationProvider;

@Order(3)
@Configuration
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter{


	@Autowired
	AjaxAuthenticationProvider ajaxAuthenticationProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(ajaxAuthenticationProvider);
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.antMatcher("/api/**")
			.authorizeRequests()
            .antMatchers("/api/messages").hasRole("MANAGER")
            .antMatchers("/api/config").hasRole("ADMIN")
            .antMatchers("/api/login").permitAll()
			.anyRequest().authenticated()
			;

		http
			.exceptionHandling()
			.authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
			.accessDeniedHandler(ajaxAccessDeniedHandler())
//		    .and()
//    		.addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
    		;

		http.csrf().disable()
		;
		customConfigureAjax(http);

	}


	private void customConfigureAjax(HttpSecurity http) throws Exception {
		http
			.apply(new AjaxLoginConfigure<>())
			.successHandlerAjax(authenticationSuccessHandler())
			.failureHandlerAjax(authenticationFailureHandler())
			.setAuthenticationManager(authenticationManager())
			.loginProcessingUrl("/api/login")
		;
	}


	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

//	@Bean
//	public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
//		AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
//		ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
//		ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
//		ajaxLoginProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
//		return ajaxLoginProcessingFilter;
//	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AjaxSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new AjaxFailureHandler();
	}

	@Bean
	public AccessDeniedHandler ajaxAccessDeniedHandler() {
		return new AjaxAccessDeniedHandler();
	}

}
