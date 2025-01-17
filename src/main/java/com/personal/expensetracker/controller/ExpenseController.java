package com.personal.expensetracker.controller;

import com.personal.expensetracker.model.Category;
import com.personal.expensetracker.model.Expense;
import com.personal.expensetracker.model.User;
import com.personal.expensetracker.model.UserPrincipal;
import com.personal.expensetracker.service.CategoryService;
import com.personal.expensetracker.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/expense/add")
    private String addExpense(Model model) {
        model.addAttribute("catList", categoryService.getAllCategories());
        return "AddExpenseForm";
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

            // Check if the user is logged in
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserPrincipal up = (UserPrincipal) auth.getPrincipal();

            User user = up.getUser();


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
