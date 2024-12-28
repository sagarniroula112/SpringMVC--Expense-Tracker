package com.personal.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personal.expensetracker.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
