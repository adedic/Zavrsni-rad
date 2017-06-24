package hr.tvz.cimernik.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.tvz.cimernik.db.BillRepository;
import hr.tvz.cimernik.db.CategoryRepository;
import hr.tvz.cimernik.db.RoomateGroupRepository;
import hr.tvz.cimernik.db.UserRepository;
import hr.tvz.cimernik.model.RoomateGroup;
import hr.tvz.cimernik.model.User;

@Controller
@RequestMapping("/members")
public class MemberCtrl {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoomateGroupRepository groupRepository;
	@Autowired
	BillRepository billRepository;
	@Autowired
	CategoryRepository categoryRepository;

	@GetMapping("/new")
	String showFormMember(Model model, Principal principal) {
		User user = userRepository.findOneByUsername(principal.getName());
		if (user.getRoomateGroup() == null) {
			return "redirect:/group/dashboard";
		}
		model.addAttribute("member", new User());
		return "newMember";
	}

	@PostMapping("/new")
	String addNewMember(Model model, Principal principal, @ModelAttribute("member") User formMember) {
		User user = userRepository.findOneByUsername(principal.getName());
		RoomateGroup group = user.getRoomateGroup();
		List<User> members = group.getMembers();
		User member = userRepository.findOneByUsername(formMember.getUsername());
		
		if (MainCtrl.validateUserInputError(model, members, formMember.getUsername(), member)) {
			return "newMember";
		} 
		group.setMembers(members);

		member.setRoomateGroup(group);

		userRepository.save(member);
		groupRepository.save(group);

		return "redirect:/group/dashboard?memberSuccess=true";
	}

}
