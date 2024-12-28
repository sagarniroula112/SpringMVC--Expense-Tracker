package com.personal.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personal.expensetracker.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
}
