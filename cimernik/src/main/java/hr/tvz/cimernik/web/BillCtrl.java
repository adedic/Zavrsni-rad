package hr.tvz.cimernik.web;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hr.tvz.cimernik.db.BillRepository;
import hr.tvz.cimernik.db.CategoryRepository;
import hr.tvz.cimernik.db.RoomateGroupRepository;
import hr.tvz.cimernik.db.UserRepository;
import hr.tvz.cimernik.model.Bill;
import hr.tvz.cimernik.model.Category;
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

	@GetMapping("/deleteBill/{id}")
	String deleteBill(@PathVariable Integer id, Model model, Principal principal) {
		billRepository.delete(billRepository.findOne(id));

		Integer userId = userRepository.findOneByUsername(principal.getName()).getId();
		return "redirect:/group/bills/" + userId + "?deleteSuccess=true";
	}
	
	@GetMapping("/editBill/{id}")//
	String getEditBill(@PathVariable Integer id, Model model, Principal principal) {
		if(userRepository.findOneByUsername(principal.getName()).getRoomateGroup() == null){
			return "redirect:/";
		}

		return "redirect:/dashboard?editBillSuccess=true";
	}
	
	@PostMapping("/group/editBill/{id}")
	String editBill(@PathVariable Integer id, Model model, Principal principal, @ModelAttribute("bill") Bill bill) {
		List<Category> categories = categoryRepository.findAll();
		categories.remove(0);
		model.addAttribute("categories", categories);
	
		User user = userRepository.findOneByUsername(principal.getName());
		Bill oldBill = billRepository.findOne(id);
		billRepository.delete(billRepository.findOne(id));
		bill = new Bill(user, user.getRoomateGroup(), bill.getTitle(), bill.getPrice(), oldBill.getDateCreated(), new Date(),
				bill.getDescription(), bill.getCategory());

		billRepository.save(bill);
		
		return "redirect:/group/dashboard?billEdit=true";
	}

	@GetMapping("/bill/new")
	String showFormAddBill(Model model, Principal principal) {

		if(userRepository.findOneByUsername(principal.getName()).getRoomateGroup() == null){
			return "redirect:/";
		}
		model.addAttribute("bill", new Bill());
		List<Category> categories = categoryRepository.findAll();
		categories.remove(0);
		model.addAttribute("categories", categories);

		return "newBill";
	}

	@PostMapping("/bill/new")
	public String addNewBill(Model model, @Valid @ModelAttribute("bill") Bill bill, BindingResult bindingResult,
			Principal principal) {
		List<Category> categories = categoryRepository.findAll();
		categories.remove(0);
		model.addAttribute("categories", categories);
		boolean error = bindingResult.hasErrors();
		if (error) {
			return "newBill";
		}
		User user = userRepository.findOneByUsername(principal.getName());
		bill = new Bill(user, user.getRoomateGroup(), bill.getTitle(), bill.getPrice(), new Date(),
				bill.getDescription(), bill.getCategory());

		billRepository.save(bill);
		return "redirect:/group/dashboard";

	}

}
