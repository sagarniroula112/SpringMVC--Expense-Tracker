package com.personal.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personal.expensetracker.model.Expense;

import java.util.List;
import com.personal.expensetracker.model.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUser(User u);
    void deleteById(Long id);
}
