package com.personal.expensetracker.serviceimpl;

import java.util.List;

import com.personal.expensetracker.model.Category;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.expensetracker.model.User;
import com.personal.expensetracker.model.Expense;
import com.personal.expensetracker.repository.ExpenseRepository;
import com.personal.expensetracker.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	@Autowired
	private ExpenseRepository expRepo;

	@Override
	public void addExpense(Expense e) {
		// TODO Auto-generated method stub
		expRepo.save(e);
	}

	@Override
	public void deleteExpense(Long id) {
		// TODO Auto-generated method stub
		Expense expense = expRepo.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
		expRepo.deleteById(id);
	}

	@Override
	public void editExpense(Expense e) {
		// TODO Auto-generated method stub
		expRepo.save(e);
	}

	@Override
	public Expense getExpenseById(Long id) {
		// TODO Auto-generated method stub
		return expRepo.findById(id).get();
	}

	@Override
	public List<Expense> getExpensesByUser(User u) {
		return expRepo.findAllByUser(u);
	}


	@Override
	public List<Expense> getAllExpenses() {
		// TODO Auto-generated method stub
		return expRepo.findAll();
	}

}
