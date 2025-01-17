package com.personal.expensetracker.serviceimpl;

import java.util.List;

import com.personal.expensetracker.config.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.personal.expensetracker.model.User;
import com.personal.expensetracker.repository.UserRepository;
import com.personal.expensetracker.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	@Override
	public void addUser(User u) {
		// TODO Auto-generated method stub
		u.getRoles().add(Role.USER);
		u.setPassword(encoder.encode(u.getPassword()));
		userRepo.save(u);
	}

	@Override
	public User findByUsernameAndPassword(String email, String password) {
		return userRepo.findByUsernameAndPassword(email, password);
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

	@Override
	public User getByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public User getByUsername(String username) {
		return userRepo.findByUsername(username);
	}

}
