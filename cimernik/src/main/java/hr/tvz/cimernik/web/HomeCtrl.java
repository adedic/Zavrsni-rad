package hr.tvz.cimernik.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public final class HomeCtrl {
	@GetMapping("/")
	String home(){
		return "redirect:/group/dashboard";
	}
}
