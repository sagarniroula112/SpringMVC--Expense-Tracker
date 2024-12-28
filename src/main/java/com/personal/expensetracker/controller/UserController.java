package com.personal.expensetracker.controller;

import com.personal.expensetracker.model.User;
import com.personal.expensetracker.service.ExpenseService;
import com.personal.expensetracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
	@Autowired
	private ExpenseService expService;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	private String getDashboard(Model model, User u, HttpSession session) {
		User user = (User) session.getAttribute("activeUser");
		model.addAttribute("expList", expService.getExpensesByUser(user));
		return "Dashboard";
	}

	@GetMapping("/user/login")
	private String login() {
		return "LoginForm";
	}

	@PostMapping("/user/login")
	private String afterLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
		String encrPw = DigestUtils.md5DigestAsHex(password.getBytes());
		User user = userService.findByEmailAndPassword(email, encrPw);

		if(user != null) {
			session.setAttribute("activeUser", user);
			session.setMaxInactiveInterval(300);
			model.addAttribute("loginName", user.getFname());
			return "redirect:/";
		}
		else{
			return "LoginForm";
		}
	}

	@GetMapping("/user/logout")
	private String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/user/login";
	}

	@GetMapping("/user/signup")
	private String signup(){
		return "SignupForm";
	}

	@PostMapping("/user/signup")
	private String aftersignup(User user){
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
//		model.addAttribute("user", user);
		userService.addUser(user);
		return "redirect:/user/login";
	}
}
