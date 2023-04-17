package com.security.security.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@GetMapping("/")
	public String index(HttpSession session) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		Authentication authentication1 = null;

		if(context != null ) {
			authentication1 = context.getAuthentication();
			authentication1.getPrincipal();
			authentication1.getCredentials();
		}
		System.out.println(authentication == authentication1);

		return "home";
	}

	@GetMapping("/thread")
	public String thread() {

		new Thread(() ->{
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		}).start();

		return "thread";
	}


	@GetMapping("/loginPage")
	public String loginPage() {
		return "loginPage";
	}

	@GetMapping("/user")
	public String user() {

		return "user";
	}

	@GetMapping("/admin/pay")
	public String admin_pay() {

		return "admin pay";
	}

	@GetMapping("/admin/all")
	public String admin_all() {

		return "admin";
	}

	@GetMapping("/login")
	public String login() {

		return "login";
	}

	@GetMapping("/denied")
	public String denied() {

		return "denied";
	}

}
