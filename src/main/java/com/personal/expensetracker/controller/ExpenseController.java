package com.personal.expensetracker.controller;

import com.personal.expensetracker.model.Category;
import com.personal.expensetracker.model.Expense;
import com.personal.expensetracker.model.User;
import com.personal.expensetracker.model.UserPrincipal;
import com.personal.expensetracker.service.CategoryService;
import com.personal.expensetracker.service.ExpenseService;
import com.personal.expensetracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseService expService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @GetMapping("/expense/add")
    private String addExpense(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication != null && authentication.isAuthenticated()) {
			// Get the UserPrincipal (or User object) from the authentication
			Object principal = authentication.getPrincipal();

			if(principal instanceof UserPrincipal) {
				model.addAttribute("username", ((UserPrincipal) authentication.getPrincipal()).getUser().getUsername());
                
                model.addAttribute("catList", categoryService.getAllCategories());
				return "AddExpenseForm";
			} else if(principal instanceof OAuth2User) {
			
				OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
				
				String username = oauth2User.getAttribute("login");
				String email = oauth2User.getAttribute("email");
			
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
				}

				if(email == null) {
					model.addAttribute("username", oauth2User.getAttribute("login"));
				}
				if(username == null){
					model.addAttribute("username", oauth2User.getAttribute("email"));
				}
                model.addAttribute("catList", categoryService.getAllCategories());
                return "AddExpenseForm";

            }}
        
        return "Failure";
    }



    @PostMapping("/expense/add")
    public String addExpense(Model model, Expense expense, HttpSession session,
                             @RequestParam("categoryId") Long categoryId, RedirectAttributes redirectAttributes) {
        try {
            // Check if the category is valid
            if (categoryId == null || categoryId <= 0) {
                redirectAttributes.addFlashAttribute("error", "Invalid category.");
                return "redirect:/expense/add";
            }

            // Set category to the expense
            expense.setCategory(categoryService.getCategoryById(categoryId));

            // Check if the user is logged in (either regular login or OAuth)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();

            User user = null;

            if (principal instanceof UserPrincipal) {
                // Regular user login (via username/password)
                UserPrincipal userPrincipal = (UserPrincipal) principal;
                user = userPrincipal.getUser();
            } else if (principal instanceof OAuth2User) {
                // OAuth2 user login (e.g., Google, GitHub)
                OAuth2User oauth2User = (OAuth2User) principal;
                String username = null;

                if(oauth2User.getAttribute("login") != null) {
                    username = oauth2User.getAttribute("login"); // GitHub's "login" or "email" for Google
                    user = userService.getByUsername(username);
                }

                if(oauth2User.getAttribute("email") != null) {
                    username = oauth2User.getAttribute("email"); // GitHub's "login" or "email" for Google
                    user = userService.getByEmail(username);
                }

                if (user == null) {
                    // Create new user if it doesn't exist
                    user = new User();
                    user.setUsername(username);
                    user.setEmail(oauth2User.getAttribute("email")); // Retrieve email if available
                    user.setPassword(""); // OAuth2 users usually don't have passwords
                    userService.addUser(user);
                }
            }

            // If no user found, redirect to login page
            if (user == null) {
                redirectAttributes.addFlashAttribute("loginMsg", "You need to LOG IN FIRST!!!");
                return "redirect:/expense/add";
            }

            // Set user to the expense and save
            expense.setUser(user);
            expService.addExpense(expense);

            // Redirect to the dashboard
            return "redirect:/";

        } catch (Exception e) {
            // Log the exception and show an error message to the user
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "An error occurred while adding the expense.");
            return "redirect:/expense/add";
        }
    }



    @GetMapping("/expense/edit")
    private String editExpense(){
        return "EditExpenseForm";
    }

    @GetMapping("/expense/delete")
    private String deleteExpense(@RequestParam Long id){
        System.out.println(id);
        expService.deleteExpense(id);
        return "redirect:/";
    }
}
