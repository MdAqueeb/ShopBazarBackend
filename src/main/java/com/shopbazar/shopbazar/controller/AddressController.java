package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.entity.Address;
import com.shopbazar.shopbazar.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
@Tag(name = "Address", description = "Endpoints for managing user shipping and billing addresses")
public class AddressController {

    private final AddressService addressService;

    // 1. Create a new address for a user
    @Operation(summary = "Create address", description = "Adds a new address to a user's profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Address>> createAddress(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId, @RequestBody Address address) {
        Address createdAddress = addressService.createAddress(userId, address);
        return new ResponseEntity<>(com.shopbazar.shopbazar.dto.ApiResponse.<Address>builder()
                .success(true)
                .message("Address created successfully")
                .data(createdAddress)
                .build(), HttpStatus.CREATED);
    }

    // 2. Get all addresses for a user
    @Operation(summary = "Get user addresses", description = "Retrieves all addresses associated with a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<List<Address>>> getUserAddresses(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        List<Address> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<List<Address>>builder()
                .success(true)
                .message("Addresses retrieved successfully")
                .data(addresses)
                .build());
    }

    // 3. Get a specific address for a user
    @Operation(summary = "Get address by ID", description = "Retrieves details of a specific address for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User or address not found")
    })
    @GetMapping("/{addressId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Address>> getAddressById(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the address", required = true)
            @PathVariable Long addressId) {
        Address address = addressService.getAddressByIdAndUserId(addressId, userId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Address>builder()
                .success(true)
                .message("Address retrieved successfully")
                .data(address)
                .build());
    }

    // 4. Update an address
    @Operation(summary = "Update address", description = "Updates an existing address record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "404", description = "User or address not found")
    })
    @PutMapping("/{addressId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Address>> updateAddress(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the address to update", required = true)
            @PathVariable Long addressId, @RequestBody Address addressDetails) {
        Address updatedAddress = addressService.updateAddress(userId, addressId, addressDetails);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Address>builder()
                .success(true)
                .message("Address updated successfully")
                .data(updatedAddress)
                .build());
    }

    // 5. Delete an address
    @Operation(summary = "Delete address", description = "Removes an address from a user's profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User or address not found")
    })
    @DeleteMapping("/{addressId}")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Void>> deleteAddress(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the address to delete", required = true)
            @PathVariable Long addressId) {
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Void>builder()
                .success(true)
                .message("Address deleted successfully")
                .data(null)
                .build());
    }

    // 6. Set address as default
    @Operation(summary = "Set default address", description = "Sets a specific address as the default for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Default address updated successfully"),
            @ApiResponse(responseCode = "404", description = "User or address not found")
    })
    @PutMapping("/{addressId}/default")
    public ResponseEntity<com.shopbazar.shopbazar.dto.ApiResponse<Address>> setDefaultAddress(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the address to set as default", required = true)
            @PathVariable Long addressId) {
        Address updatedAddress = addressService.setDefaultAddress(userId, addressId);
        return ResponseEntity.ok(com.shopbazar.shopbazar.dto.ApiResponse.<Address>builder()
                .success(true)
                .message("Default address updated successfully")
                .data(updatedAddress)
                .build());
    }
}
