package com.personal.expensetracker.service;

import java.util.List;

import com.personal.expensetracker.model.User;

public interface UserService {
	void addUser(User u);
	User findByUsernameAndPassword(String username, String password);
	void editUser(User u);
	void deleteUser(long id);
	User getUserById(long id);
	List<User> getAllUsers();
	User getByEmail(String email);
	User getByUsername(String username);
}
