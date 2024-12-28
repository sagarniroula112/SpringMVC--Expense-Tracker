package com.personal.expensetracker.service;

import java.util.List;

import com.personal.expensetracker.model.Category;

public interface CategoryService {
	void addCategory(Category c);
	void deleteCategory(Long id);
	void editCategory(Category c);
	Category getCategoryById(Long id);
	List<Category> getAllCategories();
}
