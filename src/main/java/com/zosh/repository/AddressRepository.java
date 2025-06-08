package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.modal.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
    List<Address> findByUserId(Long userId); // <-- Add this line
}
