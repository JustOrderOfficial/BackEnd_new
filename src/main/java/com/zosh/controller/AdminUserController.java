package com.zosh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.UserDTO;
import com.zosh.exception.UserException;
import com.zosh.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;

    // Endpoint to get all users (admin only)
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader("Authorization") String jwt) throws UserException {
        try {
            // Fetch all users for admin (this should be secured via the JWT and user role check)
            List<UserDTO> users = userService.findAllUsers(); 
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (UserException e) {
            // Handle specific user exception for unauthorized access
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); // Return 403 Forbidden if not admin
        } catch (Exception e) {
            // Handle any other generic errors
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
