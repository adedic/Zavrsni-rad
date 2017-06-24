package hr.tvz.cimernik.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

import hr.tvz.cimernik.db.BillRepository;
import hr.tvz.cimernik.db.CategoryRepository;
import hr.tvz.cimernik.db.RoomateGroupRepository;
import hr.tvz.cimernik.db.UserRepository;
import hr.tvz.cimernik.model.Bill;
import hr.tvz.cimernik.model.Category;
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
	
	
	// svi računi korisnika
	@GetMapping("/bills/{id}")
	String showUserBills(@PathVariable Integer id, Model model, Principal principal,
			@RequestParam(value = "deleteSuccess", required = false) String deleteSuccess) {
		User user = userRepository.findOne(id);
		boolean isRoomate = true;
		if(user.equals(userRepository.findOneByUsername(principal.getName()))){
			isRoomate=false;
		}
		model.addAttribute("isRoomate",isRoomate);
		model.addAttribute("user", user);

		if (user.getRoomateGroup() == null) {
			return "redirect:dashboard";
		}
		// račune po mjesecima
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

	@GetMapping("/deleteBill/{id}")
	String deleteBill(@PathVariable Integer id, Model model, Principal principal) {
		billRepository.delete(billRepository.findOne(id));
		return "redirect:/group/dashboard?deleteSuccess=true";
	}

	@GetMapping("/dashboard")
	String showDashboard(Model model, Principal principal,
			@RequestParam(value = "payoffSuccess", required = false) String payoffSuccess,
			@RequestParam(value = "leaveSuccess", required = false) String leaveSuccess,
			@RequestParam(value = "groupSuccess", required = false) String groupSuccess,
			@RequestParam(value = "deleteSuccess", required = false) String deleteSuccess,
			@RequestParam(value = "memberSuccess", required = false) String memberSuccess) {

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
			model.addAttribute("membersExists", membersExists);
		}

		model.addAttribute("groupExists", groupExists);
		List<Bill> bills = billRepository.findAllByUser(user);
		List<Bill> groupBills = billRepository.findAllByRoomateGroup(currentGroup);
		boolean groupBillsExists = !groupBills.isEmpty();

		if (groupBillsExists) {
			model.addAttribute("groupBills", groupBills);
		}
		model.addAttribute("groupBillsExists", groupBillsExists);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int currentMonth = cal.get(Calendar.MONTH);

		BigDecimal monthExpense = new BigDecimal(0);

		for (Bill b : bills) {
			cal.setTime(b.getDateCreated());
			if (cal.get(Calendar.MONTH) == currentMonth) {
				monthExpense = (monthExpense.add(b.getPrice()));
			}
		}

		model.addAttribute("monthExpense", monthExpense);
		boolean hasDebt = debt(user).compareTo(new BigDecimal(0)) < 0;
		model.addAttribute("hasDebt", hasDebt);
		model.addAttribute("debt", debt(user).abs());

		model.addAttribute("payoffSuccess", payoffSuccess);
		model.addAttribute("leaveSuccess", leaveSuccess);
		model.addAttribute("groupSuccess", groupSuccess);
		model.addAttribute("deleteSuccess", deleteSuccess);
		model.addAttribute("memberSuccess", memberSuccess);

		return "dashboard";
	}

	@PostMapping("/leave")
	String removeGroup(Model model, Principal principal) {

		// ako je grupa napuštena provjeri za pozvane usere je li imaju
		// inviterId od tog usera prije nego se ulogira
		// i ako ima stavi na null

		// kad se poziva novi user- naci njegov id i postaviti mu inviterId na
		// tog usera koji ga poziva

		User user = userRepository.findOneByUsername(principal.getName());
		RoomateGroup group = user.getRoomateGroup();

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

	@GetMapping("/new")
	String showFormNewGroup(Model model, Principal principal) {
		model.addAttribute("roomateGroup", new RoomateGroup());
		User user = userRepository.findOneByUsername(principal.getName());

		if (user.getRoomateGroup() != null) {
			return "redirect:dashboard";
		}
		return "newGroup";
	}
	
	@PostMapping("/search")
	String findGroup(Model model, Principal principal, @RequestParam("name") String name){
		List<RoomateGroup> groups = groupRepository.findAll().stream().filter(g -> g.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
	
		model.addAttribute("groups",groups);
		
		
		return "groups";
	}

	@PostMapping("/new")
	String saveGroup(Model model, Principal principal, @RequestParam("member[]") List<String> userStrings,
			@Valid @ModelAttribute("roomateGroup") RoomateGroup roomateGroup, BindingResult bindingResult) {
		boolean error = bindingResult.hasErrors();
		List<User> members = new ArrayList<>();
		members.add(userRepository.findOneByUsername(principal.getName()));

		
		for (String userString : userStrings) {

			User u = userRepository.findOneByUsername(userString);
			error = validateUserInputError(model, members, userString, u);
			if(error){
				return "newGroup";
			}
		}

		RoomateGroup newGroup = new RoomateGroup(roomateGroup.getName(), members);
		groupRepository.save(newGroup);
		for (User u : members) {
			u.setRoomateGroup(newGroup);
			userRepository.save(u);
		}

		return "redirect:dashboard?groupSuccess=true";
	}
	public static boolean validateUserInputError(Model model,List<User> members, String memberString, User member){
	
		String errorText = "";
		if (memberString.equals("")) {
			errorText += "Moraš unijeti korisničko ime cimera.";
			model.addAttribute("errorText", errorText);
			return true;

		}
		// provjera je li user postoji
		boolean userNoExists = member == null;
		if (userNoExists) {
			model.addAttribute("userNoExists", true);
			errorText += "Moraš unijeti postojeće korisničko ime cimera.";
			model.addAttribute("errorText", errorText);
			return true;
		} else if (!userNoExists) {
			model.addAttribute("userNoExists", false);

			// provjera je li user vec u nekoj grupi
			if (member.getRoomateGroup() != null) {
				model.addAttribute("userHasGroup", true);
				errorText += "Korisnik već ima grupu!";
				model.addAttribute("errorText", errorText);
				return true;
			} else if (member.getRoomateGroup() == null) {
				model.addAttribute("userHasGroup", false);
				members.add(member);
				

			}
			
		}
		return false;
	}

	@Autowired
	CategoryRepository cr;

	public BigDecimal debt(User user) {

		BigDecimal sum = billRepository.findAllByRoomateGroup(user.getRoomateGroup()).stream()
				.map(bill -> bill.getPrice()).reduce(new BigDecimal(0), (a, b) -> a.add(b));
		if (user.getRoomateGroup() == null) {
			return new BigDecimal(0);
		}
		BigDecimal avg = sum.divide(new BigDecimal(user.getRoomateGroup().getMembers().size()), 2, RoundingMode.HALF_UP);

		BigDecimal expenses = billRepository.findAllByUserAndRoomateGroup(user, user.getRoomateGroup()).stream()
				.map(bill -> bill.getPrice()).reduce(new BigDecimal(0), (a, b) -> a.add(b));

		return expenses.subtract(avg);
	}

	public boolean payoff(User user) {
		BigDecimal debt = debt(user);
		if (debt.compareTo(new BigDecimal(0)) >= 0) {
			return false;
		}
		List<User> creditors = user.getRoomateGroup().getMembers().stream()
				.filter(m -> debt(m).compareTo(new BigDecimal(0)) > 0).collect(Collectors.toList());
		BigDecimal creditSum = creditors.stream().map(c -> debt(c)).reduce(new BigDecimal(0), (a, b) -> a.add(b));

		Date date = new Date();
		Category payoffCategory = cr.findOne(1);

		List<Bill> bills = creditors.stream().map(c -> {
			BigDecimal credit = debt(c);
			BigDecimal creditRatio = credit.divide(creditSum, 2, RoundingMode.HALF_UP);
			BigDecimal payedCredit = creditRatio.multiply(debt);
			return new Bill(c, c.getRoomateGroup(), "Isplata duga", payedCredit, date, "", payoffCategory);
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
