//package com.example.orderproduct.service.impl;
//
//import com.example.orderproduct.constrant.MessageConst;
//import com.example.orderproduct.dto.request.ProductRequestDTO;
//import com.example.orderproduct.dto.response.BaseReponseDTO;
//import com.example.orderproduct.dto.response.PaginationReponseDTO;
//import com.example.orderproduct.dto.response.ProductReponseDTO;
//import com.example.orderproduct.entity.ProductCategoryEntity;
//import com.example.orderproduct.entity.ProductEntity;
//import com.example.orderproduct.entity.ProductTagEntity;
//import com.example.orderproduct.mapper.ProductMapper;
//import com.example.orderproduct.repository.ProductCategoryRepository;
//import com.example.orderproduct.repository.ProductRepository;
//import com.example.orderproduct.repository.ProductTagRepository;
//import com.example.orderproduct.service.ProductService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Locale;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//public class ProductServiceImpl implements ProductService {
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private ProductMapper productMapper;
//    @Autowired
//    private ProductCategoryRepository productCategoryRepository;
//    @Autowired
//    private ProductTagRepository productTagRepository;
//    @Autowired
//    private MessageSource messageSource;
//
//    @Override
//    public BaseReponseDTO<List<ProductEntity>> getAllProducts(int page, int size, Locale locale) {
//        try{
//            int offset = page * size;
//            int totalElements = productRepository.countPagin();
//            int totalPage  = (int) Math.ceil((double) totalElements / size);
//            List<ProductEntity> reponse = productRepository.findAll(offset, size);
//            if(reponse.isEmpty()){
//                return BaseReponseDTO.<List<ProductEntity>>builder()
//                        .code(-1)
//                        .isSuccess(false)
//                        .message(messageSource.getMessage(MessageConst.LIST_PRODUCT_NOT_FOUND,null, locale))
//                        .pagination(null)
//                        .data(reponse)
//                        .build();
//            }
//            PaginationReponseDTO paginationReponseDTO = PaginationReponseDTO.builder()
//                    .totalItems(totalElements)
//                    .totalPages(totalPage)
//                    .currentPage(page)
//                    .size(size)
//                    .build();
//
//            return BaseReponseDTO.<List<ProductEntity>>builder()
//                    .code(0)
//                    .isSuccess(true)
//                    .message(messageSource.getMessage(MessageConst.GET_DATA_SUCCESS,null, locale))
//                    .pagination(paginationReponseDTO)
//                    .data(reponse)
//                    .build();
//        }catch (Exception e){
//            return BaseReponseDTO.<List<ProductEntity>>builder()
//                    .code(-2)
//                    .isSuccess(false)
//                    .message(e.getMessage())
//                    .pagination(null)
//                    .data(null)
//                    .build();
//        }
//
//    }
//
//    @Override
//    public BaseReponseDTO<ProductReponseDTO> getProductById(Long id, Locale locale) {
//        try {
//            ProductEntity productEntity = productRepository.findById(id).orElse(null);
//            if(productEntity == null) {
//                return BaseReponseDTO.<ProductReponseDTO>builder()
//                        .code(-1)
//                        .isSuccess(false)
//                        .message(messageSource.getMessage(MessageConst.PRODUCT_NOT_FOUND,null, locale))
//                        .data(null)
//                        .build();
//            }
//            List<Long> categoryId = productCategoryRepository.findByProductId(id).stream().map(ProductCategoryEntity::getCategoryId).collect(Collectors.toList());
//            List<Long> tagId = productTagRepository.findByProductId(productEntity.getId()).stream().map(ProductTagEntity::getTagId).collect(Collectors.toList());
//            ProductReponseDTO productReponseDTO = productMapper.toReponseDTO(productEntity, categoryId, tagId);
//            return BaseReponseDTO.<ProductReponseDTO>builder()
//                    .code(0)
//                    .isSuccess(true)
//                    .message(messageSource.getMessage(MessageConst.GET_DATA_SUCCESS,null, locale))
//                    .data(productReponseDTO)
//                    .build();
//        } catch (Exception e) {
//            return BaseReponseDTO.<ProductReponseDTO>builder()
//                    .code(-2)
//                    .isSuccess(false)
//                    .message(e.getMessage())
//                    .data(null)
//                    .build();
//        }
//    }
//
//    @Override
//    public BaseReponseDTO<ProductReponseDTO> createProduct(ProductRequestDTO productRequestDTO, Locale locale) {
//       try {
//           ProductReponseDTO reponseDTO = productMapper.createProdct(productRequestDTO,productRequestDTO.getCategoryIds(),productRequestDTO.getTagIds());
//           return BaseReponseDTO.<ProductReponseDTO>builder()
//                   .code(0)
//                   .isSuccess(true)
//                   .message(messageSource.getMessage(MessageConst.CREATE_PRODUCT_SUCCESS,null,locale))
//                   .data(reponseDTO)
//                   .build();
//       }
//       catch (Exception e) {
//           return BaseReponseDTO.<ProductReponseDTO>builder()
//                   .code(-2)
//                   .isSuccess(false)
//                   .message(e.getMessage())
//                   .data(null)
//                   .build();
//       }
//    }
//    @Override
//    public BaseReponseDTO<ProductReponseDTO> updateProduct(ProductRequestDTO updateProductRequestDTO, Locale locale){
//        try{
//            ProductReponseDTO reponseDTO = productMapper.updateProduct(updateProductRequestDTO, updateProductRequestDTO.getCategoryIds(), updateProductRequestDTO.getCategoryIds());
//            return BaseReponseDTO.<ProductReponseDTO>builder()
//                    .code(0)
//                    .isSuccess(true)
//                    .message(messageSource.getMessage(MessageConst.UPDATE_PRODUCT_SUCCESS,null,locale))
//                    .data(reponseDTO)
//                    .build();
//        }
//        catch (Exception e) {
//            return BaseReponseDTO.<ProductReponseDTO>builder()
//                    .code(-2)
//                    .isSuccess(false)
//                    .message(e.getMessage())
//                    .data(null)
//                    .build();
//        }
//    }
//}
