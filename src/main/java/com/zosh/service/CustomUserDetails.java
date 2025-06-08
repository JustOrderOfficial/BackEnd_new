//package com.zosh.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.zosh.modal.User;
//import com.zosh.repository.UserRepository;
//
//@Service
//public class CustomUserDetails implements UserDetailsService {
//	
//	private UserRepository userRepository;
//	
//	public CustomUserDetails(UserRepository userRepository) {
//		this.userRepository=userRepository;
//		
//	}
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//	    // Find the user by email (returns Optional<User>)
//	    Optional<User> userOptional = userRepository.findByEmail(username);
//
//	    // If the user is not present, throw UsernameNotFoundException
//	    User user = userOptional.orElseThrow(() -> 
//	        new UsernameNotFoundException("User not found with email " + username));
//
//	    // Prepare authorities (for now, an empty list is used, can be updated as needed)
//	    List<GrantedAuthority> authorities = new ArrayList<>();
//	    
//	    // Return a new org.springframework.security.core.userdetails.User instance
//	    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
//	}
//
//
//}
