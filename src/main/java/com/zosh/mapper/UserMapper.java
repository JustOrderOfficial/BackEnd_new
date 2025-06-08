package com.zosh.mapper;

import com.zosh.dto.UserDTO;
import com.zosh.dto.AddressDTO;
import com.zosh.modal.User;
import com.zosh.modal.Address;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    // Convert User entity to UserDTO
    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setMobile(user.getMobile());
        userDTO.setCreatedAt(user.getCreatedAt());

        // Map addresses from User entity to List<AddressDTO>
        List<AddressDTO> addressDTOs = user.getAddresses().stream()
                .map(address -> {
                    // Convert Address entity to AddressDTO
                    AddressDTO addressDTO = new AddressDTO();
                    addressDTO.setId(address.getId());
                    addressDTO.setFirstName(address.getFirstName());
                    addressDTO.setLastName(address.getLastName());
                    addressDTO.setStreetAddress(address.getStreetAddress());
                    addressDTO.setCity(address.getCity());
                    addressDTO.setState(address.getState());
                    addressDTO.setZipCode(address.getZipCode());
                    addressDTO.setMobile(address.getMobile());
                    return addressDTO;
                })
                .collect(Collectors.toList());

        userDTO.setAddresses(addressDTOs); // Set the list of AddressDTOs in UserDTO

        return userDTO;
    }

    // Convert UserDTO to User entity
    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setMobile(userDTO.getMobile());

        // Convert List<AddressDTO> in UserDTO to List<Address> entities
        List<Address> addresses = userDTO.getAddresses().stream()
                .map(addressDTO -> {
                    Address address = new Address();
                    address.setId(addressDTO.getId());
                    address.setFirstName(addressDTO.getFirstName());
                    address.setLastName(addressDTO.getLastName());
                    address.setStreetAddress(addressDTO.getStreetAddress());
                    address.setCity(addressDTO.getCity());
                    address.setState(addressDTO.getState());
                    address.setZipCode(addressDTO.getZipCode());
                    address.setMobile(addressDTO.getMobile());
                    return address;
                })
                .collect(Collectors.toList());

        user.setAddresses(addresses); // Set the list of Address entities in User entity

        return user;
    }
}
