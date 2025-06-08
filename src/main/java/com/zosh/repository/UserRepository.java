package com.zosh.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.modal.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
    Optional<User> findByEmail(String email); // Return Optional<User>
	
	public List<User> findAllByOrderByCreatedAtDesc();

}
