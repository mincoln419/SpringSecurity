package com.security.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@GetMapping("/")
	public String index() {
		return "home";
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
