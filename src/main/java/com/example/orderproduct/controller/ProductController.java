//package com.example.orderproduct.controller;
//
//import com.example.orderproduct.dto.request.ProductRequestDTO;
//import com.example.orderproduct.dto.response.BaseReponseDTO;
//import com.example.orderproduct.dto.response.ProductReponseDTO;
//import com.example.orderproduct.entity.ProductEntity;
//import com.example.orderproduct.service.ProductService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Locale;
//
//@RestController
//@RequestMapping("/product")
//@RequiredArgsConstructor
//public class ProductController {
//    private final ProductService productService;
//
//    @GetMapping("/all")
//    public BaseReponseDTO<List<ProductEntity>> getAllProducts(String search,int page, int size, Locale locale) {
//        BaseReponseDTO<List<ProductEntity>> products = productService.getAllProducts(page, size, locale);
//        return products;
//    }
//    @GetMapping("/by-id")
//    public BaseReponseDTO<ProductReponseDTO> getProductById(Long id, Locale locale) {
//        BaseReponseDTO<ProductReponseDTO> productReponseDTO = productService.getProductById(id, locale);
//        return productReponseDTO;
//    }
//    @PostMapping("/create")
//    public BaseReponseDTO<ProductReponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO, Locale locale) {
//        BaseReponseDTO<ProductReponseDTO> reponseDTO = productService.createProduct(productRequestDTO,locale);
//        return reponseDTO;
//    }
//    @PostMapping("/update")
//    public BaseReponseDTO<ProductReponseDTO> updateProduct(@RequestBody ProductRequestDTO updateProductRequestDTO, Locale locale) {
//        BaseReponseDTO<ProductReponseDTO> reponseDTO = productService.updateProduct(updateProductRequestDTO, locale);
//        return reponseDTO;
//    }
//}
