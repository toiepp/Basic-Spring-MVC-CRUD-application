package me.mikholsky.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class ErrorsController {
	@GetMapping("/403")
	public String forbidden() {
		return "error-pages/403-forbidden";
	}

	@GetMapping("/404")
	public String notFound() {
		return "error-pages/404-not-found";
	}

	@GetMapping("/405")
	public String notAllowed() {
		return "error-pages/405-not-allowed";
	}
}
