package com.shopbazar.shopbazar.controller;

import com.shopbazar.shopbazar.entity.Transaction;
import com.shopbazar.shopbazar.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Endpoints for viewing user and platform financial transactions")
public class TransactionController {

        private final TransactionService transactionService;

        @Operation(summary = "Get transaction by ID", description = "Retrieves details of a specific financial transaction")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Transaction details retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Transaction not found")
        })
        @GetMapping("/{transactionId}")
        public ResponseEntity<Transaction> getTransactionById(
                        @Parameter(description = "ID of the transaction", required = true) @PathVariable Long transactionId) {
                return transactionService.getTransactionById(transactionId)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }
}
