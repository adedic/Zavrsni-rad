package hr.tvz.cimernik.web;

import java.security.Principal;
import java.util.ArrayList;
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

	@GetMapping("/myBills")
	String showUserBills(Model model, Principal principal) {

		User user = userRepository.findOneByUsername(principal.getName());
		model.addAttribute("userCredentials", user.getName() + ' ' + user.getSurname());
		List<Bill> bills = new ArrayList<>();

		if (!billRepository.findAllByUser(user).isEmpty()) {
			bills = billRepository.findAllByUser(user);
			model.addAttribute("bills", bills);
		} else if (billRepository.findAllByUser(user).isEmpty()) {
			model.addAttribute("noUserBills", "Nemate nijedan račun.");
		}
		return "userBills";
	}
	
	@GetMapping("/deleteBill/{id}")
	String deleteBill(@PathVariable	 Integer id, Model model, Principal principal){
		billRepository.delete(billRepository.findOne(id));
		//poruka za uspjesno brisanje plus ikona za brisanje
		return "redirect:/dashboard";
		
	}
	
	@GetMapping("/dashboard")
	String showDashboard(Model model, Principal principal) {

		User user = userRepository.findOneByUsername(principal.getName());
		model.addAttribute("userCredentials", user.getName() + ' ' + user.getSurname());
		RoomateGroup currentGroup = user.getRoomateGroup();
		List<User> members = new ArrayList<>();

		if (currentGroup == null) {
			model.addAttribute("noGroup", "Još nemate grupu?");

		} else if (currentGroup != null) {
			currentGroup = user.getRoomateGroup();
			members = currentGroup.getMembers();
			model.addAttribute("group", currentGroup);
			
			
			if(members.isEmpty()){
				model.addAttribute("noMembers", "Još niste dodali cimere u grupu?");
			}
			else if(!members.isEmpty()){
				model.addAttribute("memebers", members);
			}
		}

		
		List<Bill> bills = new ArrayList<>();

		List<Bill> groupBills = new ArrayList<>();

		if (!billRepository.findAllByUser(user).isEmpty()) {
			bills = billRepository.findAllByUser(user);
		} else if (billRepository.findAllByUser(user).isEmpty()) {
			model.addAttribute("noUserBills", "Nemate nijedan račun.");
		}
		if (!billRepository.findAllByRoomateGroup(currentGroup).isEmpty()) {

			groupBills = billRepository.findAllByRoomateGroup(currentGroup);
		} else if (billRepository.findAllByRoomateGroup(currentGroup).isEmpty()) {
			model.addAttribute("noGroupBills", "Nema računa u grupi.");
		}

		model.addAttribute("group", currentGroup);
		model.addAttribute("groupBills", groupBills);
		model.addAttribute("bills", bills);
		return "proba";
	}

	@ModelAttribute("roomateGroup")
	public RoomateGroup getNewRoomateGroup() {
		return new RoomateGroup();
	}

	@GetMapping("/newGroup")
	String newGroup(Model model, Principal principal) {
		User user = userRepository.findOneByUsername(principal.getName());
		model.addAttribute("userCredentials", user.getName() + ' ' + user.getSurname());

		// max 10 membera po grupi
		// svaki member je novi input
		// kako poslati vise istih Objekata iz forme u polje
		return "newGroup";
	}

	@PostMapping("/newGroup")
	String saveGroup(Model model, Principal principal, @RequestParam("member[]") List<String> userStrings,
			@ModelAttribute RoomateGroup group) {
		List<User> members = new ArrayList<>();

		RoomateGroup rGroup = new RoomateGroup(group.getName());
		for (String userString : userStrings) {
			System.out.println(userString);
			User u = userRepository.findOneByUsername(userString);
			// Member m = new Member(rGroup,u );
			members.add(u);
		}
		// RoomateGroup rGroup = new RoomateGroup(group.getName(),members);

		for (User u : members)
			System.out.println(u.getName() + " " + u.getSurname());

		System.out.println(rGroup.getName());

		groupRepository.save(new RoomateGroup(group.getName()));
		// memberRepository.save(rGroup);

		return "redirect:/dashboard";
	}

}
