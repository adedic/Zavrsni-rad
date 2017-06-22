package hr.tvz.cimernik.web;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hr.tvz.cimernik.db.BillRepository;
import hr.tvz.cimernik.db.CategoryRepository;
import hr.tvz.cimernik.db.RoomateGroupRepository;
import hr.tvz.cimernik.db.UserRepository;
import hr.tvz.cimernik.model.Bill;
import hr.tvz.cimernik.model.RoomateGroup;
import hr.tvz.cimernik.model.User;

@Controller
public final class MainCtrl {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoomateGroupRepository groupRepository;
	@Autowired
	BillRepository billRepository;
	@Autowired
	CategoryRepository categoryRepository;

	// svi računi korisnika ulogiranog
	@GetMapping("/bills")
	String showUserBills(Model model, Principal principal) {
		User user = userRepository.findOneByUsername(principal.getName());
		model.addAttribute("userCredentials", user.getName() + ' ' + user.getSurname());

		if (user.getRoomateGroup() == null) {
			return "redirect:/dashboard";
		}
		// ako nema grupe preusmjeriti na dashboard
		List<Bill> bills = new ArrayList<>();

		if (!billRepository.findAllByUser(user).isEmpty()) {
			bills = billRepository.findAllByUser(user);
			model.addAttribute("bills", bills);
			model.addAttribute("billsExists", true);
		} else if (billRepository.findAllByUser(user).isEmpty()) {
			model.addAttribute("billsExists", false);
		}
		return "userBills";
	}

	@GetMapping("/deleteBill/{id}")
	String deleteBill(@PathVariable Integer id, Model model, Principal principal) {
		billRepository.delete(billRepository.findOne(id));
		// poruka za uspjesno brisanje plus ikona za brisanje
		return "redirect:/dashboard";

	}

	@GetMapping("/dashboard")
	String showDashboard(Model model, Principal principal) {
		model.addAttribute("rommateGroup", new RoomateGroup());

		model.addAttribute("name");
		User user = userRepository.findOneByUsername(principal.getName());
		model.addAttribute("userCredentials", user.getName() + ' ' + user.getSurname());

		RoomateGroup currentGroup = null;
		List<User> members = new ArrayList<>();

		if (user.getRoomateGroup() != null) {
			currentGroup = user.getRoomateGroup();
			members = currentGroup.getMembers();
			model.addAttribute("group", currentGroup);
			model.addAttribute("groupExists", true);

		} else if (user.getRoomateGroup() == null) {
			model.addAttribute("groupExists", false);
		}

		if (members.isEmpty()) {
			model.addAttribute("membersExists", false);
		} else if (!members.isEmpty()) {
			model.addAttribute("members", members);

			model.addAttribute("membersExists", true);
		}

		List<Bill> bills = new ArrayList<>();

		List<Bill> groupBills = billRepository.findAllByRoomateGroup(currentGroup);

		if (!billRepository.findAllByUser(user).isEmpty()) {
			bills = billRepository.findAllByUser(user);
		} else if (billRepository.findAllByUser(user).isEmpty()) {
			model.addAttribute("noUserBills", "Nemate nijedan račun.");
		}
		if (!groupBills.isEmpty()) {

			model.addAttribute("groupBills", groupBills);
			model.addAttribute("groupBillsExists", true);

		} else if (groupBills.isEmpty()) {
			model.addAttribute("groupBillsExists", false);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int currentMonth = cal.get(Calendar.MONTH);
		System.out.println(currentMonth);

		BigDecimal monthExpense = new BigDecimal(0);

		for (Bill b : bills) {
			cal.setTime(b.getDateCreated());
			if (cal.get(Calendar.MONTH) == currentMonth) {
				System.out.println("cijena" + b.getPrice());
				monthExpense = (monthExpense.add(b.getPrice()));

				System.out.println(cal.get(Calendar.MONTH));
				System.out.println(monthExpense);
			}
		}

		model.addAttribute("monthExpense", monthExpense);
		model.addAttribute("group", currentGroup);
		return "dashboard";
	}

	@PostMapping("/group")
	String saveGroup(Model model, Principal principal, @RequestParam("member[]") List<String> userStrings,
			@ModelAttribute RoomateGroup group) {
		List<User> members = new ArrayList<>();
		members.add(userRepository.findOneByUsername(principal.getName()));

		for (String userString : userStrings) {
			User u = userRepository.findOneByUsername(userString);
			members.add(u);
		}
		System.out.println(group.getName());

		RoomateGroup newGroup = new RoomateGroup(group.getName(), members);
		groupRepository.save(newGroup);

		for (User u : members) {
			System.out.println(u.getName() + " " + u.getSurname());
			u.setRoomateGroup(newGroup);
			userRepository.save(u);
		}
		return "redirect:/dashboard";
	}

}
