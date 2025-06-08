package com.zosh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.zosh.dto.UserDTO;
import com.zosh.exception.UserException;
import com.zosh.mapper.UserMapper;
import com.zosh.modal.User;
import com.zosh.repository.UserRepository;
import com.zosh.user.domain.UserRole;

@Component
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;

    private CartService cartService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializationComponent(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder,
                                       CartService cartService) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
        this.cartService=cartService;
    }

    @Override
    public void run(String... args) throws UserException {
        initializeAdminUser();
    }

    private void initializeAdminUser() throws UserException {
        String adminUsername = "codewithzosh@gmail.com";

        if (userRepository.findByEmail(adminUsername) == null) {
            User adminUser = new User();

            adminUser.setPassword(passwordEncoder.encode("codewithzosh"));
            adminUser.setFirstName("zosh");
            adminUser.setLastName("code");
            adminUser.setEmail(adminUsername);
            adminUser.setRole(UserRole.ADMIN.toString());

            User admin = userRepository.save(adminUser);

            // Convert User to UserDTO
            UserDTO adminDTO = UserMapper.toDTO(admin);

            // Call createCart with UserDTO
            cartService.createCart(adminDTO);
        }
    }


    }


