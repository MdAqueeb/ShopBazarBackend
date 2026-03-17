package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.entity.Order;
import com.shopbazar.shopbazar.entity.Payment;
import com.shopbazar.shopbazar.exception.*;
import com.shopbazar.shopbazar.repository.OrderRepository;
import com.shopbazar.shopbazar.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.getOrderId()));

        if ("SUCCESS".equals(order.getPaymentStatus())) {
            throw new ConflictException("Order is already paid");
        }

        Optional<Payment> existingPayment = paymentRepository.findByOrder_OrderId(request.getOrderId());
        Payment payment;
        if (existingPayment.isPresent()) {
            payment = existingPayment.get();
            if (payment.getPaymentStatus() == Payment.PaymentStatus.SUCCESS) {
                throw new ConflictException("Payment already completed for this order");
            }
            payment.setAmount(request.getAmount());
            payment.setPaymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod()));
            payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        } else {
            payment = Payment.builder()
                    .order(order)
                    .amount(request.getAmount())
                    .paymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod()))
                    .paymentStatus(Payment.PaymentStatus.PENDING)
                    .build();
        }

        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    public PaymentResponse getPaymentDetails(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        return mapToResponse(payment);
    }

    @Transactional
    public PaymentResponse verifyPayment(PaymentVerifyRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + request.getPaymentId()));

        if (payment.getPaymentStatus() == Payment.PaymentStatus.SUCCESS) {
            throw new ConflictException("Payment already verified");
        }

        payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
        payment.setGatewayTransaction(request.getGatewayTransactionId());
        
        Order order = payment.getOrder();
        order.setPaymentStatus("SUCCESS");
        orderRepository.save(order);

        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse refundPayment(PaymentRefundRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + request.getPaymentId()));

        if (payment.getPaymentStatus() == Payment.PaymentStatus.REFUNDED) {
            throw new ConflictException("Payment already refunded");
        }

        if (payment.getPaymentStatus() != Payment.PaymentStatus.SUCCESS) {
            throw new BadRequestException("Only successful payments can be refunded");
        }

        payment.setPaymentStatus(Payment.PaymentStatus.REFUNDED);
        
        Order order = payment.getOrder();
        order.setPaymentStatus("REFUNDED");
        orderRepository.save(order);

        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod().name())
                .paymentStatus(payment.getPaymentStatus().name())
                .gatewayTransaction(payment.getGatewayTransaction())
                .build();
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
