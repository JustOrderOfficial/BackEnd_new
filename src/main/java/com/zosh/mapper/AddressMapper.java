package com.zosh.mapper;

import com.zosh.dto.AddressDTO;
import com.zosh.modal.Address;

public class AddressMapper {

    // Convert Address entity to AddressDTO
    public static AddressDTO toDTO(Address address) {
        return new AddressDTO(
            address.getId(),
            address.getFirstName(),
            address.getLastName(),
            address.getStreetAddress(),
            address.getCity(),
            address.getState(),
            address.getZipCode(),
            address.getMobile()
        );
    }

    // Convert AddressDTO to Address entity
    public static Address toEntity(AddressDTO addressDTO) {
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
    }
}
