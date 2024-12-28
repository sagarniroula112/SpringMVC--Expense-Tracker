package com.personal.expensetracker.service;

import java.util.List;

import com.personal.expensetracker.model.Category;
import com.personal.expensetracker.model.Expense;
import jakarta.servlet.http.HttpSession;
import com.personal.expensetracker.model.User;

public interface ExpenseService {
	void addExpense(Expense e);
	void deleteExpense(Long id);
	void editExpense(Expense e);
	Expense getExpenseById(Long id);
	List<Expense> getExpensesByUser(User u);
	List<Expense> getAllExpenses();
}
