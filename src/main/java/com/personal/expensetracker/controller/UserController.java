package com.personal.expensetracker.controller;

import com.personal.expensetracker.model.User;
import com.personal.expensetracker.model.UserPrincipal;
import com.personal.expensetracker.service.ExpenseService;
import com.personal.expensetracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
			Object principal = authentication.getPrincipal();

			if(principal instanceof UserPrincipal) {
				System.out.println("Regular login------------");
				UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal(); // Assuming UserPrincipal implements UserDetails
				User currentUser = userPrincipal.getUser();

				model.addAttribute("expList", expService.getExpensesByUser(currentUser));
				model.addAttribute("username", ((UserPrincipal) authentication.getPrincipal()).getUser().getUsername());

				return "Dashboard";
			} else if(principal instanceof OAuth2User) {
				System.out.println("OAuth2 login------------");
				System.out.println("Check 1--------------");
				OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
				System.out.println(oauth2User);
				String username = oauth2User.getAttribute("login");
				String name = oauth2User.getAttribute("name");
				String email = oauth2User.getAttribute("email");
				System.out.println(username + " " + name);
				System.out.println("EMAIL: "+ email);
				System.out.println("Check 2-------------");
				User user = null;
				if(email == null) {
					user = userService.getByUsername(username);
				}
				if(username == null){
					user = userService.getByEmail(email);
				}

				if(user == null) {
					user = new User();
					if (email != null && email.contains("@")) {
						// Extract the part before @gmail.com
						username = email.split("@")[0];
					}
					user.setUsername(username);
					user.setEmail(email);
					user.setPassword("");
					userService.addUser(user);
				}

				model.addAttribute("expList", expService.getExpensesByUser(user));
				if(email == null) {
					model.addAttribute("username", oauth2User.getAttribute("login"));
				}
				if(username == null){
					model.addAttribute("username", oauth2User.getAttribute("email"));
				}

				return "Dashboard";
			}
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
