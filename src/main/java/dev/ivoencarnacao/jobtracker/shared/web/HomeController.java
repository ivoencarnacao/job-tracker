package dev.ivoencarnacao.jobtracker.shared.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/register")
	public String register() {
		return "identity/register";
	}

	@GetMapping("/login")
	public String login() {
		return "identity/login";
	}

}
