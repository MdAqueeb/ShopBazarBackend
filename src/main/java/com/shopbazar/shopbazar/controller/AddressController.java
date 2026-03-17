package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.dto.ApiResponse;
import com.shopbazar.shopbazar.entity.Address;
import com.shopbazar.shopbazar.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // 1. Create a new address for a user
    @PostMapping
    public ResponseEntity<ApiResponse<Address>> createAddress(
            @PathVariable Long userId, @RequestBody Address address) {
        Address createdAddress = addressService.createAddress(userId, address);
        return new ResponseEntity<>(ApiResponse.<Address>builder()
                .success(true)
                .message("Address created successfully")
                .data(createdAddress)
                .build(), HttpStatus.CREATED);
    }

    // 2. Get all addresses for a user
    @GetMapping
    public ResponseEntity<ApiResponse<List<Address>>> getUserAddresses(@PathVariable Long userId) {
        List<Address> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(ApiResponse.<List<Address>>builder()
                .success(true)
                .message("Addresses retrieved successfully")
                .data(addresses)
                .build());
    }

    // 3. Get a specific address for a user
    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Address>> getAddressById(
            @PathVariable Long userId, @PathVariable Long addressId) {
        Address address = addressService.getAddressByIdAndUserId(addressId, userId);
        return ResponseEntity.ok(ApiResponse.<Address>builder()
                .success(true)
                .message("Address retrieved successfully")
                .data(address)
                .build());
    }

    // 4. Update an address
    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Address>> updateAddress(
            @PathVariable Long userId, @PathVariable Long addressId, @RequestBody Address addressDetails) {
        Address updatedAddress = addressService.updateAddress(userId, addressId, addressDetails);
        return ResponseEntity.ok(ApiResponse.<Address>builder()
                .success(true)
                .message("Address updated successfully")
                .data(updatedAddress)
                .build());
    }

    // 5. Delete an address
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable Long userId, @PathVariable Long addressId) {
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Address deleted successfully")
                .data(null)
                .build());
    }

    // 6. Set address as default
    @PutMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<Address>> setDefaultAddress(
            @PathVariable Long userId, @PathVariable Long addressId) {
        Address updatedAddress = addressService.setDefaultAddress(userId, addressId);
        return ResponseEntity.ok(ApiResponse.<Address>builder()
                .success(true)
                .message("Default address updated successfully")
                .data(updatedAddress)
                .build());
    }
}
