package hr.tvz.cimernik.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.tvz.cimernik.db.UserRepository;
import hr.tvz.cimernik.model.User;

@Controller
public class RegisterCtrl {
	@Autowired
	UserRepository userRepository;

	@GetMapping("/register")
	public String registerForm() {
		return "register";

	}

	@PostMapping("/register")
	public String register(@RequestParam("name") String name, @RequestParam("surname") String surname,
			@RequestParam("phone") String phone, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("password2") String password2,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (userRepository.countByUsername(username) > 0) {
			redirectAttributes.addFlashAttribute("taken", true);
			return "redirect:/register";
		}
		if (!password.equals(password2)) {
			redirectAttributes.addFlashAttribute("mismatch", true);
			return "redirect:register";
		}
		String regexStr = "^[0-9]{8,10}$";
		if(phone != null){
			if (!phone.matches(regexStr)){
				redirectAttributes.addFlashAttribute("phoneNotDigit", true);
				return "redirect:/register";
			}
		}
		

			
		User user = new User(username, true);
		user.setName(name);
		user.setSurname(surname);
		user.setPhone(phone);
		user.setPassword(password);
		user.setRoleList("ROLE_USER");

		System.out.println(name);
		System.out.println(surname);
		System.out.println(username);
		System.out.println(user.getPassword());

		System.out.println(password);
		userRepository.save(user);
		
		try {
			request.login(username, password);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}
}
