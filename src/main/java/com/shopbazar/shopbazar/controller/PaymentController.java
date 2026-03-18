package com.shopbazar.shopbazar.controller;
import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Endpoints for processing and verifying customer payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Initiate payment", description = "Creates a new payment transaction for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment initiated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment request")
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentRequest request) {
        return new ResponseEntity<>(paymentService.initiatePayment(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get payment details", description = "Retrieves the status and details of a specific payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(
            @Parameter(description = "ID of the payment transaction", required = true)
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentDetails(paymentId));
    }

    @Operation(summary = "Verify payment", description = "Verifies a payment transaction with the payment gateway")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment verified successfully"),
            @ApiResponse(responseCode = "400", description = "Payment verification failed")
    })
    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(@RequestBody PaymentVerifyRequest request) {
        return ResponseEntity.ok(paymentService.verifyPayment(request));
    }

    @Operation(summary = "Refund payment", description = "Issues a refund for a previously completed payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Refund request accepted"),
            @ApiResponse(responseCode = "400", description = "Refund request invalid or failed")
    })
    @PostMapping("/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@RequestBody PaymentRefundRequest request) {
        return new ResponseEntity<>(paymentService.refundPayment(request), HttpStatus.ACCEPTED);
    }
}
