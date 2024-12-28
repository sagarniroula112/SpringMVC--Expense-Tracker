package com.personal.expensetracker.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.expensetracker.model.Category;
import com.personal.expensetracker.repository.CategoryRepository;
import com.personal.expensetracker.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository catRepo;

	@Override
	public void addCategory(Category c) {
		// TODO Auto-generated method stub
		catRepo.save(c);
	}

	@Override
	public void deleteCategory(Long id) {
		// TODO Auto-generated method stub
		catRepo.deleteById(id);
	}

	@Override
	public void editCategory(Category c) {
		// TODO Auto-generated method stub
		catRepo.save(c);
	}

	@Override
	public Category getCategoryById(Long id) {
		// TODO Auto-generated method stub
		return catRepo.findById(id).get();
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return catRepo.findAll();
	}

}
