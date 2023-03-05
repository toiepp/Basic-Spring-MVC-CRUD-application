package me.mikholsky.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorsController {
	@GetMapping("/403")
	public String forbidden() {
		return "error-pages/403-forbidden";
	}

	@GetMapping("/404")
	public String notFound() {
		return "error-pages/404-not-found";
	}
}
