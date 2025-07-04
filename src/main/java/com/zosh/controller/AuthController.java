package com.zosh.controller;

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

import com.zosh.service.AuthService;
import com.zosh.utill.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${frontend.url}", allowCredentials = "true")

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

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

    
    
}
