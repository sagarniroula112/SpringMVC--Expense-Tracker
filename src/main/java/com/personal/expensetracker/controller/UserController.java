package com.personal.expensetracker.controller;

import com.personal.expensetracker.model.User;
import com.personal.expensetracker.model.UserPrincipal;
import com.personal.expensetracker.service.ExpenseService;
import com.personal.expensetracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private String getDashboard(Model model) {
		// Retrieve the authenticated user from SecurityContext
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);

		if(authentication != null && authentication.isAuthenticated()) {
			// Get the UserPrincipal (or User object) from the authentication
			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal(); // Assuming UserPrincipal implements UserDetails
			User currentUser = userPrincipal.getUser();

			// Fetch expenses based on the user and add to the model
			model.addAttribute("expList", expService.getExpensesByUser(currentUser));
			model.addAttribute("username", ((UserPrincipal) authentication.getPrincipal()).getUser().getUsername());

			return "Dashboard"; // Your dashboard view
		}

		return "Failure";
	}



	@GetMapping("/user/login")
	private String login() {
		return "LoginForm";
	}

	@GetMapping("/user/logout")
	private String logout(HttpSession session) {
		return "redirect:/user/login";
	}

	@GetMapping("/user/signup")
	private String signup(){
		return "SignupForm";
	}

	@PostMapping("/user/signup")
	private String aftersignup(User user){
		userService.addUser(user);
		return "redirect:/user/login";
	}
}
