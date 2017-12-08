package com.example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class SecuritySampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuritySampleApplication.class, args);
	}
	
	@GetMapping("/")
	public String index() {
		return("login");
	}
	
	@GetMapping("/logout.html")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		// 明示的に色々消しても、ちゃんとログアウトできていない…(;;)
		
		System.out.println("*** loghout!");
		HttpSession session = request.getSession();
		session.invalidate();
		SecurityContextHolder.clearContext();
		SecurityContextHolder.getContext().setAuthentication(null);
		
		return("login");
	}
}
