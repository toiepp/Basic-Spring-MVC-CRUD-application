package me.mikholsky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/authenticate")
public class AuthenticationController {
	private UserDetailsService userDetailsService;

	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@GetMapping("/login")
	public String login() {
		return "login-page";
	}

	@PostMapping("/login/process")
	public RedirectView authenticate(@RequestParam String username, @RequestParam String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		if (userDetails.getPassword().equals(password)) {
			return new RedirectView("/");
		}
		return new RedirectView("/login?failure");
	}
}
