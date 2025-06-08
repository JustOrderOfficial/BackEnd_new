package com.zosh.service;

import java.util.List;

import com.zosh.dto.AddressDTO;
import com.zosh.exception.AddressException;
import com.zosh.modal.User;

public interface AddressService {

    // Add a new address for the logged-in user
    AddressDTO addAddress(User user, AddressDTO addressDTO);

    // Get all addresses of the logged-in user
    List<AddressDTO> getUserAddresses(User user);

    // Update a specific address (only if the user owns it)
    AddressDTO updateAddress(Long addressId, AddressDTO updatedDTO, User user) throws AddressException;

    // Delete a specific address (only if the user owns it)
    void deleteAddress(Long addressId, User user) throws AddressException;
}
