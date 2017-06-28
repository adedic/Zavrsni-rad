package hr.tvz.cimernik.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.tvz.cimernik.db.BillRepository;
import hr.tvz.cimernik.db.CategoryRepository;
import hr.tvz.cimernik.db.InviteRepository;
import hr.tvz.cimernik.db.RoomateGroupRepository;
import hr.tvz.cimernik.db.UserRepository;
import hr.tvz.cimernik.model.Bill;
import hr.tvz.cimernik.model.Category;
import hr.tvz.cimernik.model.Invite;
import hr.tvz.cimernik.model.RoomateGroup;
import hr.tvz.cimernik.model.User;

@Controller
@RequestMapping("/group")
public final class MainCtrl {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoomateGroupRepository groupRepository;
	@Autowired
	BillRepository billRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	InviteRepository inviteRepository;

	// svi računi korisnika
	@GetMapping("/bills/{id}")
	String showUserBills(@PathVariable Integer id, Model model, Principal principal,
			@RequestParam(value = "deleteSuccess", required = false) String deleteSuccess) {
		User user = userRepository.findOne(id);

		if (user.getRoomateGroup() == null) {
			return "redirect:/";
		}
		boolean isRoomate = true;
		if (user.equals(userRepository.findOneByUsername(principal.getName()))) {
			isRoomate = false;
		}
		model.addAttribute("isRoomate", isRoomate);
		model.addAttribute("user", user);

		if (user.getRoomateGroup() == null) {
			return "redirect:dashboard";
		}

		List<Bill> bills = billRepository.findAllByUser(user);
		boolean billsExists = !bills.isEmpty();
		if (billsExists) {
			bills = billRepository.findAllByUser(user);
			model.addAttribute("bills", bills);
		}

		model.addAttribute("billsExists", billsExists);
		model.addAttribute("deleteSuccess", deleteSuccess);
		return "userBills";
	}

	// brisanje računa
	@GetMapping("/deleteBill/{id}")
	String deleteBill(@PathVariable Integer id) {
		billRepository.delete(billRepository.findOne(id));
		return "redirect:/group/dashboard?deleteSuccess=true";
	}

	// odbijanje invitea
	@GetMapping("/deleteInvite/{id}")
	String deleteInvite(@PathVariable Integer id, Principal principal) {
		inviteRepository.delete(inviteRepository.findOne(id));
		return "redirect:/group/dashboard?deletedInvite=true";
	}

	// prihvat clana u grupu - sam posalo zahtjev
	@GetMapping("/joinMember/{memberId}/{userId}/{inviteId}")
	String joinMember(@PathVariable("memberId") Integer memberId, @PathVariable("userId") Integer userId,
			@PathVariable("inviteId") Integer inviteId, Model model, Principal principal) {
		User member = userRepository.findOne(memberId);
		User user = userRepository.findOne(userId);
		RoomateGroup group = user.getRoomateGroup();
		List<User> members = group.getMembers();
		members.add(member);
		group.setMembers(members);
		member.setRoomateGroup(group);
		userRepository.save(member);
		groupRepository.save(group);
		inviteRepository.delete(inviteRepository.findOne(inviteId));
		return "redirect:/group/dashboard?memberSuccess=true";

	}

	// pozvani član prihvatio poziv u grupu
	@GetMapping("/addMember/{memberId}/{inviterId}/{groupId}/{inviteId}")
	String addMemberAcceptedInvite(@PathVariable("memberId") Integer memberId,
			@PathVariable("inviterId") Integer inviterId, @PathVariable("groupId") Integer groupId, Model model,
			@PathVariable("inviteId") Integer inviteId, Principal principal) {

		User member = userRepository.findOne(memberId);
		RoomateGroup group = null;
		User inviter = userRepository.findOne(inviterId);
		if (inviter.getRoomateGroup() != null) {
			group = inviter.getRoomateGroup();
		}

		group = groupRepository.findOne(groupId);

		List<User> members = new ArrayList<>();
		if (group.getMembers() != null) {
			members = group.getMembers();
		}
		members.add(member);
		group.setMembers(members);
		member.setRoomateGroup(group);
		userRepository.save(member);
		groupRepository.save(group);

		inviteRepository.delete(inviteRepository.findOne(inviteId));

		return "redirect:/group/dashboard?memberSuccess=true";
	}

	@GetMapping("/dashboard")
	String showDashboard(Model model, Principal principal,
			@RequestParam(value = "payoffSuccess", required = false) String payoffSuccess,
			@RequestParam(value = "leaveSuccess", required = false) String leaveSuccess,
			@RequestParam(value = "groupSuccess", required = false) String groupSuccess,
			@RequestParam(value = "deleteSuccess", required = false) String deleteSuccess,
			@RequestParam(value = "memberSuccess", required = false) String memberSuccess,
			@RequestParam(value = "memberInvite", required = false) String memberInvite) {

		User user = userRepository.findOneByUsername(principal.getName());
		model.addAttribute("user", user);

		RoomateGroup currentGroup = null;
		List<User> members = new ArrayList<>();

		boolean groupExists = user.getRoomateGroup() != null;
		if (groupExists) {
			currentGroup = user.getRoomateGroup();
			model.addAttribute("group", currentGroup);

			members = currentGroup.getMembers();
			boolean membersExists = !members.isEmpty();
			if (membersExists) {
				model.addAttribute("members", members);
			}


			// member success- dobrodosli
			//
			/*if (inviteRepository.findAllByRoomateGroupAndMember(currentGroup, user).isEmpty()) {
				model.addAttribute("memberSuccess", true);
				memberSuccess = "true";
			}*/
		}
		// pozivi na pridruzenje poslani od grupe
		List<Invite> invitesFromGroup = inviteRepository.findAllByRoomateGroup(currentGroup);
		// zahtjevi za pridruženje poslani od drugih usera
		List<Invite> invitesFromUsers = inviteRepository.findAllByMember(user);

		if (!invitesFromUsers.isEmpty()) {
			invitesFromUsers = invitesFromUsers.stream()
					.filter(i -> i.getInviter() != null && i.getMember().equals(user)).collect(Collectors.toList());
		}
		model.addAttribute("invitesFromUsers", invitesFromUsers);
		model.addAttribute("invitesFromGroup", invitesFromGroup);

		model.addAttribute("groupExists", groupExists);
		// račune po GODINI pa mjesecima mapa

		LinkedHashMap <String, List<Bill>> billsMap = new LinkedHashMap<String, List<Bill>>();


		billsMap.put("Siječanj", billRepository.findAllByDateCreatedLike(1, currentGroup));
		billsMap.put("Veljača", billRepository.findAllByDateCreatedLike(2, currentGroup));
		billsMap.put("Ožujak", billRepository.findAllByDateCreatedLike(3, currentGroup));
		billsMap.put("Travanj", billRepository.findAllByDateCreatedLike(4, currentGroup));
		billsMap.put("Svibanj", billRepository.findAllByDateCreatedLike(5, currentGroup));
		billsMap.put("Lipanj", billRepository.findAllByDateCreatedLike(6, currentGroup));
		billsMap.put("Srpanj", billRepository.findAllByDateCreatedLike(7, currentGroup));
		billsMap.put("Kolovoz", billRepository.findAllByDateCreatedLike(8, currentGroup));
		billsMap.put("Rujan", billRepository.findAllByDateCreatedLike(9, currentGroup));
		billsMap.put("Listopad", billRepository.findAllByDateCreatedLike(10, currentGroup));
		billsMap.put("Studeni", billRepository.findAllByDateCreatedLike(11, currentGroup));
		billsMap.put("Prosinac", billRepository.findAllByDateCreatedLike(12, currentGroup));
		
		LinkedHashMap <String, List<Bill>> userBillsMap = new LinkedHashMap<String, List<Bill>>();
		userBillsMap.put("Siječanj", billRepository.findAllByDateCreatedAndUserLike(1, currentGroup, user));
		userBillsMap.put("Veljača", billRepository.findAllByDateCreatedAndUserLike(2, currentGroup, user));
		userBillsMap.put("Ožujak", billRepository.findAllByDateCreatedAndUserLike(3, currentGroup, user));
		userBillsMap.put("Travanj", billRepository.findAllByDateCreatedAndUserLike(4, currentGroup, user));
		userBillsMap.put("Svibanj", billRepository.findAllByDateCreatedAndUserLike(5, currentGroup, user));
		userBillsMap.put("Lipanj", billRepository.findAllByDateCreatedAndUserLike(6, currentGroup, user));
		userBillsMap.put("Srpanj", billRepository.findAllByDateCreatedAndUserLike(7, currentGroup, user));
		userBillsMap.put("Kolovoz", billRepository.findAllByDateCreatedAndUserLike(8, currentGroup, user));
		userBillsMap.put("Rujan", billRepository.findAllByDateCreatedAndUserLike(9, currentGroup, user));
		userBillsMap.put("Listopad", billRepository.findAllByDateCreatedAndUserLike(10, currentGroup, user));
		userBillsMap.put("Studeni", billRepository.findAllByDateCreatedAndUserLike(11, currentGroup, user));
		userBillsMap.put("Prosinac", billRepository.findAllByDateCreatedAndUserLike(12, currentGroup, user));
	
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int currentMonth = cal.get(Calendar.MONTH);
		
		List<Bill> bills = billRepository.findAllByUser(user);
		List<Bill> groupBills = billRepository.findAllByRoomateGroup(currentGroup);
		List<Bill> groupBillsMonth = new ArrayList<>();
		model.addAttribute("billsMap", billsMap);
		model.addAttribute("userBillsMap", userBillsMap);
		
		for (Map.Entry<String, List<Bill>> entry : billsMap.entrySet()) {
			if (entry.getValue() != null) {
				for (Bill b : entry.getValue()) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(b.getDateCreated());
					int month = cal1.get(Calendar.MONTH);
					if(month == currentMonth){
						groupBillsMonth.add(b);
						model.addAttribute("groupBillsMonthMap", entry);
					}
				}
			}
		}

		BigDecimal monthExpense = new BigDecimal(0);
		
		for (Map.Entry<String, List<Bill>> entry : userBillsMap.entrySet()) {
			if (entry.getValue() != null) {
				for (Bill b : entry.getValue()) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(b.getDateCreated());
					int month = cal1.get(Calendar.MONTH);
					if(month == currentMonth){
						groupBillsMonth.add(b);
						monthExpense = (monthExpense.add(b.getPrice()));
						model.addAttribute("userBillsMonthMap", entry);
					}
				}
			}
		}


		
		boolean groupBillsExists = !groupBills.isEmpty();

		if (groupBillsExists) {
			model.addAttribute("groupBills", groupBillsMonth);//bez month
		}
		model.addAttribute("groupBillsExists", groupBillsExists);

		model.addAttribute("monthExpense", monthExpense);
		boolean hasDebt = debt(user).compareTo(new BigDecimal(0)) < 0;
		model.addAttribute("hasDebt", hasDebt);
		model.addAttribute("debt", debt(user).abs().setScale(2, RoundingMode.CEILING));

		model.addAttribute("payoffSuccess", payoffSuccess);
		model.addAttribute("leaveSuccess", leaveSuccess);
		model.addAttribute("groupSuccess", groupSuccess);
		model.addAttribute("deleteSuccess", deleteSuccess);
		model.addAttribute("memberSuccess", memberSuccess);
		model.addAttribute("memberInvite", memberInvite);

		return "dashboard";
	}

	@PostMapping("/leave")
	String removeGroup(Model model, Principal principal) {

		// ako je grupa napuštena provjeri za pozvane usere je li imaju
		// inviterId od tog usera prije nego se ulogira
		// i ako ima stavi na null
		// postaviti GRUPU na invite gdje je inviter ovaj user koji zeli
		// napustit grupu

		User user = userRepository.findOneByUsername(principal.getName());
		RoomateGroup group = user.getRoomateGroup();

		List<Invite> userInvites = inviteRepository.findAllByInviter(user);
		for (Invite i : userInvites) {
			i.setRoomateGroup(group);
			inviteRepository.save(i);
		}

		List<User> members = new ArrayList<>();
		for (User u : userRepository.findAllByRoomateGroup(group)) {
			if (!u.getUsername().equals(user.getUsername())) {
				members.add(u);
			}
		}
		group.setMembers(members);
		groupRepository.save(group);

		user.setRoomateGroup(null);
		userRepository.save(user);

		return "redirect:dashboard?leaveSuccess=true";
	}

	// zahtjev za pridruzivanje grupi
	@GetMapping("/joinGroup/{groupId}/{memberId}/{query}")
	String joinGroupInvite(@PathVariable("groupId") Integer groupId, @PathVariable("memberId") Integer memberId,
			@PathVariable("query") String query, RedirectAttributes redirectAttributes, Principal principal,
			Model model) {
		User member = userRepository.findOne(memberId);
		RoomateGroup group = groupRepository.findOne(groupId);

		Invite invite = new Invite(group, member, null);
		Invite invite1 = inviteRepository.findOneByRoomateGroupAndMemberAndInviter(group, member, null);

		if (invite1 != null) {
			inviteRepository.delete(invite1.getId());
			inviteRepository.save(invite);
			redirectAttributes.addFlashAttribute("memberInvite", true);
			redirectAttributes.addFlashAttribute("query", query);
			showGroupsByName(principal.getName(), query, model);
			return "/groups";
		}
		inviteRepository.save(invite);

		member.setInvites(invite);
		userRepository.save(member);

		model.addAttribute("memberInvite", true);
		redirectAttributes.addFlashAttribute("memberInvite", true);
		redirectAttributes.addFlashAttribute("query", query);
		showGroupsByName(principal.getName(), query, model);
		return "/groups";
	}

	@GetMapping("/search")
	String search() {
		return "redirect:/";
	}

	@PostMapping("/search")
	String findGroup(Model model, Principal principal, @RequestParam(value = "name", required = false) String query,
			RedirectAttributes redirectAttributes) {
		showGroupsByName(principal.getName(), query, model);

		redirectAttributes.addAttribute("query", query);

		return "/groups";
	}

	@GetMapping("/groups")
	String showGroupsByQuery(Model model, Principal principal,
			@RequestParam(value = "memberInvite", required = false) String memberInvite,
			@RequestParam(value = "query", required = true) String query, RedirectAttributes redirectAttributes) {
		showGroupsByName(principal.getName(), query, model);
		model.addAttribute("memberInvite", memberInvite);
		redirectAttributes.addFlashAttribute("memberInvite", memberInvite);
		redirectAttributes.addFlashAttribute("query", query);
		return "groups";

	}

	void showGroupsByName(String username, String query, Model model) {
		User user = userRepository.findOneByUsername(username);
		if (user.getRoomateGroup() != null) {
			model.addAttribute("groupExists", true);
		}
		List<RoomateGroup> groups = groupRepository.findAll().stream()
				.filter(g -> g.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());

		List<Invite> invitesMember = inviteRepository.findAllByMember(user);
		model.addAttribute("hasInvite", false);
		for (RoomateGroup g : groups) {
			if (!invitesMember.isEmpty()) {
				for (Invite i : invitesMember) {
					if (g.getName().equals(i.getRoomateGroup().getName())) {// id
						model.addAttribute("wantedGroup", i.getRoomateGroup());
						g.setHasInvite(true);
						model.addAttribute("hasInvite", true);
						groupRepository.save(g);
					}
				}
			}
		}
		model.addAttribute("groups", groups);
		model.addAttribute("user", user);
		model.addAttribute("query", query);
	}

	@GetMapping("/new")
	String showFormNewGroup(Model model, Principal principal) {
		model.addAttribute("roomateGroup", new RoomateGroup());
		User user = userRepository.findOneByUsername(principal.getName());

		if (user.getRoomateGroup() != null) {
			return "redirect:dashboard";
		}
		return "newGroup";
	}

	@PostMapping("/new")
	String saveGroup(Model model, Principal principal, @Valid @ModelAttribute("roomateGroup") RoomateGroup roomateGroup,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors())
			return "newGroup";

		List<User> members = new ArrayList<>();
		User currentUser = userRepository.findOneByUsername(principal.getName());
		members.add(currentUser);

		RoomateGroup newGroup = new RoomateGroup(roomateGroup.getName(), members);
		groupRepository.save(newGroup);

		currentUser.setRoomateGroup(newGroup);
		userRepository.save(currentUser);

		groupRepository.save(newGroup);

		return "redirect:dashboard?groupSuccess=true";
	}

	@Autowired
	CategoryRepository cr;

	public BigDecimal debt(User user) {

		BigDecimal sum = billRepository.findAllByRoomateGroup(user.getRoomateGroup()).stream()
				.map(bill -> bill.getPrice()).reduce(new BigDecimal(0), (a, b) -> a.add(b));
		if (user.getRoomateGroup() == null) {
			return new BigDecimal(0);
		}
		BigDecimal avg = sum.divide(new BigDecimal(user.getRoomateGroup().getMembers().size()), 3, // bilo
																									// 2
				RoundingMode.HALF_UP);

		BigDecimal expenses = billRepository.findAllByUserAndRoomateGroup(user, user.getRoomateGroup()).stream()
				.map(bill -> bill.getPrice()).reduce(new BigDecimal(0), (a, b) -> a.add(b));

		return expenses.subtract(avg);
	}

	public boolean payoff(User user) {

		BigDecimal debt = debt(user);
		// model.addAttribute("debt", debt);
		if (debt.compareTo(new BigDecimal(0)) >= 0) {
			return false;
		}
		// oni kojima treba novac
		List<User> creditors = user.getRoomateGroup().getMembers().stream()
				.filter(m -> debt(m).compareTo(new BigDecimal(0)) > 0).collect(Collectors.toList());
		BigDecimal creditSum = creditors.stream().map(c -> debt(c)).reduce(new BigDecimal(0), (a, b) -> a.add(b));

		Date date = new Date();
		Category payoffCategory = cr.findOne(1);

		List<Bill> bills = creditors.stream().map(c -> {
			BigDecimal credit = debt(c);
			BigDecimal creditRatio = credit.divide(creditSum, 3, RoundingMode.HALF_UP);
			BigDecimal payedCredit = creditRatio.multiply(debt);
			return new Bill(c, c.getRoomateGroup(), "Isplata duga", payedCredit.setScale(3, RoundingMode.HALF_UP), date,
					"", payoffCategory);
		}).collect(Collectors.toList());

		Bill bill = new Bill(user, user.getRoomateGroup(), "Isplata duga", debt.negate(), date, "", payoffCategory);
		bills.add(0, bill);
		billRepository.save(bills);

		return true;

	}

	@GetMapping("/payoff") // umjesto payoff
	String payoff(Model model, Principal principal) {
		boolean payoffSuccess = payoff(userRepository.findOneByUsername(principal.getName()));
		System.out.println(payoffSuccess);

		return "redirect:dashboard?payoffSuccess=true";
	}
}
