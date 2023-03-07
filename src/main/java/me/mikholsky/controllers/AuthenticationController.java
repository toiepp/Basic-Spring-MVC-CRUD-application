package me.mikholsky.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/authenticate")
public class AuthenticationController {
	@GetMapping("/login")
	public String login() {
		return "login-page";
	}

	@GetMapping("/process")
	public RedirectView process() {
		System.out.println("------------------------------------------");

		return new RedirectView("/");
	}
}
