package com.zosh.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zosh.dto.AddressDTO;
import com.zosh.exception.AddressException;
import com.zosh.mapper.AddressMapper;
import com.zosh.modal.Address;
import com.zosh.modal.User;
import com.zosh.repository.AddressRepository;
import com.zosh.repository.UserRepository;

@Service
public class AddressServiceImplementation implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImplementation(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AddressDTO addAddress(User user, AddressDTO addressDTO) {
        // Address now contains its own first name, last name, and mobile, which are passed from the AddressDTO
        Address address = AddressMapper.toEntity(addressDTO);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        // Add saved address to user's address list
        user.getAddresses().add(savedAddress);
        userRepository.save(user);

        return AddressMapper.toDTO(savedAddress);
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO updatedDTO, User user) throws AddressException {
        Address existing = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressException("Address not found with ID: " + addressId));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new AddressException("Unauthorized to update this address");
        }

        // Update address with values from the updated DTO (first name, last name, and mobile are now part of the DTO)
        existing.setStreetAddress(updatedDTO.getStreetAddress());
        existing.setCity(updatedDTO.getCity());
        existing.setState(updatedDTO.getState());
        existing.setZipCode(updatedDTO.getZipCode());
        existing.setMobile(updatedDTO.getMobile());
        existing.setFirstName(updatedDTO.getFirstName()); // Set first name from DTO
        existing.setLastName(updatedDTO.getLastName()); // Set last name from DTO

        Address updated = addressRepository.save(existing);
        return AddressMapper.toDTO(updated);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses = addressRepository.findByUserId(user.getId());

        return addresses.stream()
                .map(AddressMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAddress(Long addressId, User user) throws AddressException {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressException("Address not found with ID: " + addressId));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AddressException("Unauthorized to delete this address");
        }

        addressRepository.delete(address);
    }
}
