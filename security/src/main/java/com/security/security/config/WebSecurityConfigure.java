package com.security.security.config;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

//import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfigure {

	@Autowired
	UserDetailsService detailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
            .anyRequest()
            .authenticated();

        http
        	.formLogin()
        	//.loginPage("/loginPage")
        	.defaultSuccessUrl("/")
        	.failureUrl("/login")
        	//.usernameParameter("userId")
        	//.passwordParameter("passwd")
        	//.loginProcessingUrl("login_proc")
        	.successHandler((req, res, auth)->{
        		System.out.println("authentication " + auth.getName());
        		res.sendRedirect("/");
        	})
        	.failureHandler((req, res, ex)->{
        		System.out.println("exception " + ex.getMessage());
        		res.sendRedirect("/login");
        	})
        	.permitAll()
        	;

        http
        	.logout()
        	.logoutUrl("/logout")
        	.logoutSuccessUrl("/login")
        	.addLogoutHandler((req, res, auth) -> {
        		HttpSession session = req.getSession();
        		session.invalidate();
        	})
        	.logoutSuccessHandler((req, res, auth)-> {
        		res.sendRedirect("/login");
        	})
        	.deleteCookies("remember")
        	;

        http
        	.rememberMe()
        	.rememberMeParameter("remember")
        	.tokenValiditySeconds(3600)
        	.userDetailsService(detailsService)
        	;

        http
        	.sessionManagement()
        	.maximumSessions(1)
        	.maxSessionsPreventsLogin(false)
        	;

        http
        	.sessionManagement()
        	.sessionFixation()
        	//.none(); -> 공격당하면 큰일남
        	.changeSessionId()
        	;

        http
        	.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        http
        	.antMatcher("/shop/**")
        	.authorizeRequests()
        		.antMatchers("/shop/login","/shop/user/**").permitAll()
        		.antMatchers("/shop/login","/shop/user/**").hasRole("USER")
        		.antMatchers("/shop/login","/shop/admin/pay").access("hasRole('ADMIN')")
        		.antMatchers("/shop/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
        		.anyRequest().authenticated()
        		;

        SecurityContextHolder.getContext().getAuthentication();

        return http.build();
    }

}
