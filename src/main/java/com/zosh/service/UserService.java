package com.zosh.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zosh.dto.UserDTO;
import com.zosh.exception.UserException;
import com.zosh.modal.User;

public interface UserService {

    UserDTO findUserById(Long userId) throws UserException;

    UserDTO findUserProfileByAuth() throws UserException;  // Updated to use SecurityContext for authentication

    List<UserDTO> findAllUsers() throws UserException;

    Page<UserDTO> findAllUsers(Pageable pageable) throws UserException;
    
    UserDTO updateUserProfile(UserDTO userDTO) throws UserException;
    
    User findUserByEmail(String email) throws UserException;
    



}
