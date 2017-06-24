package hr.tvz.cimernik.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginCtrl {
	@GetMapping("/login")
	
	public String loginForm(Model model, @RequestParam(value="logout", required=false) String logout,
										 @RequestParam(value="error", required=false) String error){
		
		if(logout != null){
			model.addAttribute("logoutPoruka", "Uspješno ste se odjavili!");
		}
		if(error !=null){
			model.addAttribute("errorPoruka", "Neispravna kombinacija imena i lozinke");
		}
		return "login";
		
	}
}
