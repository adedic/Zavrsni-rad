package hr.tvz.cimernik.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import hr.tvz.cimernik.db.BillRepository;
import hr.tvz.cimernik.db.CategoryRepository;
import hr.tvz.cimernik.db.RoomateGroupRepository;
import hr.tvz.cimernik.db.UserRepository;
import hr.tvz.cimernik.model.Bill;
import hr.tvz.cimernik.model.User;

@Controller
@SessionAttributes({ "bill" })
public class BillCtrl {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoomateGroupRepository groupRepository;
	@Autowired
	BillRepository billRepository;
	@Autowired
	CategoryRepository categoryRepository;

	@GetMapping("/newBill")
	String addNewBill(Model model, Principal principal) {
		User user = userRepository.findOneByUsername(principal.getName());
		model.addAttribute("userCredentials", user.getName() + ' ' + user.getSurname());

		model.addAttribute("date");
		model.addAttribute("bill", new Bill());
		model.addAttribute("categories", categoryRepository.findAll());

		return "newBill";
	}

	@PostMapping("/newBill")
	public String handleForm(Model model, @ModelAttribute Bill bill, Principal principal) {
		
		User user = userRepository.findOneByUsername(principal.getName());
		bill = new Bill(user, user.getRoomateGroup(), bill.getTitle(), bill.getPrice(), bill.getDateCreated(),
				bill.getDescription(), bill.getCategory());
		System.out.println(bill.getTitle());
		
		billRepository.save(bill);

		return "redirect:/dashboard";

	}

}
