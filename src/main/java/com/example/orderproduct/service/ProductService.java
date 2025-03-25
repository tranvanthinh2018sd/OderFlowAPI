package com.example.orderproduct.service;

import com.example.orderproduct.dto.request.ProductRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.ProductReponseDTO;
import com.example.orderproduct.entity.ProductEntity;

import java.util.List;
import java.util.Locale;

public interface ProductService {
    BaseReponseDTO<ProductReponseDTO> getProductById(Long id, Locale locale);
    BaseReponseDTO<List<ProductEntity>> getAllProducts(int page, int size, Locale locale);
    BaseReponseDTO<ProductReponseDTO> createProduct(ProductRequestDTO productRequestDTO, Locale locale);
    BaseReponseDTO<ProductReponseDTO> updateProduct(ProductRequestDTO updateProductRequestDTO, Locale locale);
}
