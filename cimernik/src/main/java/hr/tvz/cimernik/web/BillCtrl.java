package hr.tvz.cimernik.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import hr.tvz.cimernik.db.BillRepository;
import hr.tvz.cimernik.db.CategoryRepository;
import hr.tvz.cimernik.db.RoomateGroupRepository;
import hr.tvz.cimernik.db.UserRepository;
import hr.tvz.cimernik.model.Bill;
import hr.tvz.cimernik.model.User;

@Controller
public class BillCtrl {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoomateGroupRepository groupRepository;
	@Autowired
	BillRepository billRepository;
	@Autowired
	CategoryRepository categoryRepository;

	@GetMapping("/bill/new")
	String showFormAddBill(Model model, Principal principal) {

		model.addAttribute("date");
		model.addAttribute("bill", new Bill());
		model.addAttribute("categories", categoryRepository.findAll());

		return "newBill";
	}

	@PostMapping("/bill/new")
	public String addNewBill(Model model,@RequestAttribute @ModelAttribute Bill bill, Principal principal) {

		User user = userRepository.findOneByUsername(principal.getName());
		bill = new Bill(user, user.getRoomateGroup(), bill.getTitle(), bill.getPrice(), bill.getDateCreated(),
				bill.getDescription(), bill.getCategory());
		System.out.println(bill.getTitle());

		billRepository.save(bill);

		return "redirect:/group/dashboard";

	}

	@GetMapping("/deleteBill/{id}")
	String deleteBill(@PathVariable Integer id, Model model, Principal principal) {
		billRepository.delete(billRepository.findOne(id));

		Integer userId = userRepository.findOneByUsername(principal.getName()).getId();
		System.out.println("userid" + userId.toString());
		return "redirect:/group/bills/" + userId + "?deleteSuccess=true";
	}

}
