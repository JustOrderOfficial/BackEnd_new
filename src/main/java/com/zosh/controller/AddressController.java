package com.zosh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.AddressDTO;
import com.zosh.exception.AddressException;
import com.zosh.exception.UserException;
import com.zosh.modal.User;
import com.zosh.service.AddressService;
import com.zosh.service.UserService;

@CrossOrigin(origins = "http://localhost:5176", allowCredentials = "true")
@RestController
@RequestMapping("/api/addresses") // Base URL for all address APIs
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    // ✅ Add a new address
    // URL: POST http://localhost:5454/api/addresses
    @PostMapping("/add") // Custom name for clarity
    public ResponseEntity<AddressDTO> addAddress(
            @RequestBody AddressDTO addressDTO,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User loggedInUser
    ) throws UserException {
        User user = userService.findUserByEmail(loggedInUser.getUsername());
        AddressDTO createdAddress = addressService.addAddress(user, addressDTO);
        return ResponseEntity.ok(createdAddress);
    }

    // ✅ Get all addresses for the logged-in user
    // URL: GET http://localhost:5454/api/addresses
    @GetMapping("/list") // Custom name for clarity
    public ResponseEntity<List<AddressDTO>> getUserAddresses(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User loggedInUser
    ) throws UserException {
        User user = userService.findUserByEmail(loggedInUser.getUsername());
        List<AddressDTO> addresses = addressService.getUserAddresses(user);
        return ResponseEntity.ok(addresses);
    }

    // ✅ Update an address by ID
    // URL: PUT http://localhost:5454/api/addresses/{addressId}
    @PutMapping("/update/{addressId}") // Custom name for clarity
    public ResponseEntity<AddressDTO> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressDTO addressDTO,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User loggedInUser
    ) throws UserException, AddressException {
        User user = userService.findUserByEmail(loggedInUser.getUsername());
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO, user);
        return ResponseEntity.ok(updatedAddress);
    }

    // ✅ Delete an address by ID
    // URL: DELETE http://localhost:5454/api/addresses/{addressId}
    @DeleteMapping("/delete/{addressId}") // Custom name for clarity
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long addressId,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User loggedInUser
    ) throws UserException, AddressException {
        User user = userService.findUserByEmail(loggedInUser.getUsername());
        addressService.deleteAddress(addressId, user);
        return ResponseEntity.ok("Address deleted successfully.");
    }
}
