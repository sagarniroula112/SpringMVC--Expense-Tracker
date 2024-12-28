package com.personal.expensetracker.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.expensetracker.model.User;
import com.personal.expensetracker.repository.UserRepository;
import com.personal.expensetracker.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public void addUser(User u) {
		// TODO Auto-generated method stub
		userRepo.save(u);
	}

	@Override
	public User findByEmailAndPassword(String email, String password) {
		return userRepo.findByEmailAndPassword(email, password);
	}

	@Override
	public void editUser(User u) {
		// TODO Auto-generated method stub
		userRepo.save(u);
	}

	@Override
	public void deleteUser(long id) {
		// TODO Auto-generated method stub
		userRepo.deleteById(id);
	}

	@Override
	public User getUserById(long id) {
		// TODO Auto-generated method stub
		return userRepo.findById(id).get();
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepo.findAll();
	}
	
}
