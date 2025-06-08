package com.zosh.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.UserDTO;
import com.zosh.exception.UserException;
import com.zosh.service.UserService;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Fetch the logged-in user's profile
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfileHandler() throws UserException {
        UserDTO userDTO = userService.findUserProfileByAuth();
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // ✅ Update the logged-in user's profile (renamed to profile-edit)
    @PutMapping("/profile-edit")
    public ResponseEntity<UserDTO> updateUserProfileHandler(@RequestBody UserDTO userDTO) throws UserException {
        UserDTO updatedUser = userService.updateUserProfile(userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) throws UserException {
        UserDTO user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
