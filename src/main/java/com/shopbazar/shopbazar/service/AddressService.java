package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.Address;
import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.exception.ForbiddenException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.AddressRepository;
import com.shopbazar.shopbazar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional
    public Address createAddress(Long userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            unsetOtherDefaults(user);
        } else if (user.getAddresses() == null || user.getAddresses().isEmpty()) {
            address.setIsDefault(true); // First address is always default
        }

        address.setUser(user);
        return addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public List<Address> getUserAddresses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return user.getAddresses();
    }

    @Transactional(readOnly = true)
    public Address getAddressByIdAndUserId(Long addressId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
        
        if (!address.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Access denied: You are not the owner of this address");
        }
        
        return address;
    }

    @Transactional
    public Address updateAddress(Long userId, Long addressId, Address addressDetails) {
        Address existing = getAddressByIdAndUserId(addressId, userId);
        
        if (addressDetails.getName() != null) existing.setName(addressDetails.getName());
        if (addressDetails.getPhone() != null) existing.setPhone(addressDetails.getPhone());
        if (addressDetails.getStreet() != null) existing.setStreet(addressDetails.getStreet());
        if (addressDetails.getCity() != null) existing.setCity(addressDetails.getCity());
        if (addressDetails.getState() != null) existing.setState(addressDetails.getState());
        if (addressDetails.getPostalCode() != null) existing.setPostalCode(addressDetails.getPostalCode());
        if (addressDetails.getCountry() != null) existing.setCountry(addressDetails.getCountry());
        
        // Handle default address toggle
        if (Boolean.TRUE.equals(addressDetails.getIsDefault()) && !Boolean.TRUE.equals(existing.getIsDefault())) {
            unsetOtherDefaults(existing.getUser());
            existing.setIsDefault(true);
        } else if (addressDetails.getIsDefault() != null) {
            existing.setIsDefault(addressDetails.getIsDefault());
        }

        return addressRepository.save(existing);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address existing = getAddressByIdAndUserId(addressId, userId);
        addressRepository.delete(existing);
    }

    @Transactional
    public Address setDefaultAddress(Long userId, Long addressId) {
        Address existing = getAddressByIdAndUserId(addressId, userId);
        unsetOtherDefaults(existing.getUser());
        existing.setIsDefault(true);
        return addressRepository.save(existing);
    }

    private void unsetOtherDefaults(User user) {
        if (user.getAddresses() != null) {
            for (Address addr : user.getAddresses()) {
                if (Boolean.TRUE.equals(addr.getIsDefault())) {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                }
            }
        }
    }
}
