package com.zosh.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.modal.User;
import com.zosh.repository.UserRepository;
import com.zosh.service.AuthService;
import com.zosh.utill.GoogleLoginRequest;
import com.zosh.utill.JwtUtil;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${frontend.url}", allowCredentials = "true")

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;
    
 // Inject frontend URL (optional if needed elsewhere)
    @Value("${frontend.url}")
    private String frontendUrl;

    // ✅ Register endpoint with email conflict handling
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String firstName = request.get("first_name");
        String lastName = request.get("last_name");
        String mobile = request.get("mobile");
        String password = request.get("password");
        String role = request.get("role");

        if (email == null || password == null || firstName == null || lastName == null || mobile == null || role == null) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        String result = authService.registerUser(email, firstName, lastName, mobile, password, role);

        if ("Email already exists".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        return ResponseEntity.ok(result);
    }

    //  Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Email and password are required.");
        }

        String token = authService.loginUser(email, password);

        if ("Invalid credentials".equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(token);
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
    
//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal MyUserDetails userDetails) {
//        return ResponseEntity.ok(userDetails.getUser());
//    }
    
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        String email = request.getUser().getEmail();
        String name = request.getUser().getName();
        String picture = request.getUser().getPicture();

        if (email == null) {
            return ResponseEntity.badRequest().body("Email missing in Google user info");
        }

        System.out.println("📧 Google Login Attempt: " + email);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            try {
                user = new User();
                String[] names = name.trim().split(" ", 2);
                user.setFirstName(names.length > 0 ? names[0] : "");
                user.setLastName(names.length > 1 ? names[1] : "");
                user.setEmail(email);
                user.setPassword(null);
                user.setRole("USER");
                user.setMobile(null);
                user.setCreatedAt(LocalDateTime.now());

                user = userRepository.save(user);
                System.out.println("✅ New Google user saved: " + user.getId());

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("❌ Failed to save Google user: " + e.getMessage());
            }
        } else {
            System.out.println("👤 Existing user found: " + user.getId());
        }

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);

        return ResponseEntity.ok(response);
    }




    
    
}
