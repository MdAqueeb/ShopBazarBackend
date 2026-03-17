package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.Seller;
import com.shopbazar.shopbazar.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public Seller createSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public Optional<Seller> getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller updateSeller(Long sellerId, Seller seller) {
        Seller existing = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        existing.setBusinessName(seller.getBusinessName());
        existing.setEmail(seller.getEmail());
        existing.setPhone(seller.getPhone());
        existing.setStatus(seller.getStatus());
        return sellerRepository.save(existing);
    }

    public void deleteSeller(Long sellerId) {
        sellerRepository.deleteById(sellerId);
    }
}
