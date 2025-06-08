package com.zosh.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zosh.dto.AddressDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.UserException;
import com.zosh.mapper.AddressMapper;
import com.zosh.mapper.UserMapper;
import com.zosh.modal.Address;
import com.zosh.modal.User;
import com.zosh.repository.AddressRepository;
import com.zosh.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserServiceImplementation(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    // 🔐 Helper: Get authenticated user from security context
    private User getAuthenticatedUser() throws UserException {
        org.springframework.security.core.userdetails.User principal =
            (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String email = principal.getUsername();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException("User not found with email: " + email));
    }

    // 🔐 Helper: Ensure authenticated user is ADMIN
    private void ensureAdmin(User user) throws UserException {
        if (!"ADMIN".equals(user.getRole())) {
            throw new UserException("Not authorized. Admins only.");
        }
    }

    // ✅ Fetch current logged-in user's profile
    @Override
    public UserDTO findUserProfileByAuth() throws UserException {
        User authUser = getAuthenticatedUser();
        return UserMapper.toDTO(authUser);
    }

    // ✅ Update user profile
    @Override
    public UserDTO updateUserProfile(UserDTO userDTO) throws UserException {
        User authUser = getAuthenticatedUser();

        // Update user fields
        if (userDTO.getFirstName() != null) {
            authUser.setFirstName(userDTO.getFirstName());
        }

        if (userDTO.getLastName() != null) {
            authUser.setLastName(userDTO.getLastName());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(authUser.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
            if (existingUser.isPresent()) {
                throw new UserException("Email already in use.");
            }
            authUser.setEmail(userDTO.getEmail());
        }

        // Update mobile number if provided
        if (userDTO.getMobile() != null && !userDTO.getMobile().equals(authUser.getMobile())) {
            authUser.setMobile(userDTO.getMobile());
        }

        // Handle address updates
        if (userDTO.getAddresses() != null && !userDTO.getAddresses().isEmpty()) {
            List<Address> updatedAddresses = userDTO.getAddresses().stream()
                .map(addressDTO -> {
                    Address address = addressRepository.findById(addressDTO.getId())
                        .orElse(new Address());

                    address.setFirstName(addressDTO.getFirstName());
                    address.setLastName(addressDTO.getLastName());
                    address.setStreetAddress(addressDTO.getStreetAddress());
                    address.setCity(addressDTO.getCity());
                    address.setState(addressDTO.getState());
                    address.setZipCode(addressDTO.getZipCode());
                    address.setMobile(addressDTO.getMobile());

                    address.setUser(authUser);
                    return addressRepository.save(address);
                })
                .collect(Collectors.toList());

            authUser.setAddresses(updatedAddresses);
        }

        User updatedUser = userRepository.save(authUser);
        return UserMapper.toDTO(updatedUser);
    }


    // ✅ Fetch user by ID (restricted to self or admin)
//    @Override
//    public UserDTO findUserById(Long userId) throws UserException {
//        User authUser = getAuthenticatedUser();
//        if (!"ADMIN".equals(authUser.getRole()) && !authUser.getId().equals(userId)) {
//            throw new UserException("You are not authorized to view this user's details.");
//        }
//        User targetUser = userRepository.findById(userId)
//            .orElseThrow(() -> new UserException("User not found with id " + userId));
//        return UserMapper.toDTO(targetUser);
//    }
    
    @Override
    public UserDTO findUserById(Long userId) throws UserException {
        User targetUser = userRepository.findById(userId)
            .orElseThrow(() -> new UserException("User not found with id " + userId));
        return UserMapper.toDTO(targetUser);
    }


    // ✅ Fetch all users (admin only)
    @Override
    public List<UserDTO> findAllUsers() throws UserException {
        User authUser = getAuthenticatedUser();
        ensureAdmin(authUser);

        List<User> users = userRepository.findAllByOrderByCreatedAtDesc();
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    // ✅ Fetch paginated users (admin only)
    @Override
    public Page<UserDTO> findAllUsers(Pageable pageable) throws UserException {
        User authUser = getAuthenticatedUser();
        ensureAdmin(authUser);

        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserMapper::toDTO);
    }

    @Override
    public User findUserByEmail(String email) throws UserException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found with email: " + email));
    }



}
