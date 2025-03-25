package com.example.orderproduct.repository;

import com.example.orderproduct.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
    List<ProductCategoryEntity> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
