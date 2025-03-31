//package com.example.orderproduct.mapper;
//
//import com.example.orderproduct.constrant.MessageConst;
//import com.example.orderproduct.dto.request.ProductRequestDTO;
//import com.example.orderproduct.dto.response.CategoryReponseDTO;
//import com.example.orderproduct.dto.response.ProductReponseDTO;
//import com.example.orderproduct.dto.response.TagReponseDTO;
//import com.example.orderproduct.entity.ProductCategoryEntity;
//import com.example.orderproduct.entity.ProductEntity;
//import com.example.orderproduct.entity.ProductTagEntity;
//import com.example.orderproduct.exception.ResourceNotFoundException;
//import com.example.orderproduct.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class ProductMapper {
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private ProductCategoryRepository productCategoryRepository;
//    @Autowired
//    private TagRepository tagRepository;
//    @Autowired
//    private ProductTagRepository productTagRepository;
//    public ProductReponseDTO toReponseDTO(ProductEntity productEntity, List<Long> categoryIdList, List<Long> tagIdList) {
//        ProductReponseDTO productReponseDTO = new ProductReponseDTO();
//        productReponseDTO.setId(productEntity.getId());
//        productReponseDTO.setName(productEntity.getName());
//        productReponseDTO.setPrice(productEntity.getPrice());
//        productReponseDTO.setImage(productEntity.getImage());
//        productReponseDTO.setDescription(productEntity.getDescription());
//        productReponseDTO.setStatus(productEntity.getStatus());
//        List<CategoryReponseDTO> categoryReponseDTOList = new ArrayList<>();
//        for (Long categoryId : categoryIdList) {
//            CategoryReponseDTO categoryReponseDTO = new CategoryReponseDTO();
//            categoryRepository.findById(categoryId).ifPresent(category -> {
//                categoryReponseDTO.setId(category.getId());
//                categoryReponseDTO.setName(category.getName());
//                categoryReponseDTO.setDescription(category.getDescription());
//                categoryReponseDTO.setStatus(category.getStatus());
//                categoryReponseDTOList.add(categoryReponseDTO);
//            });
//        }
//        List<TagReponseDTO> tagReponseDTOList = new ArrayList<>();
//        for (Long tagId : tagIdList) {
//            TagReponseDTO tagReponseDTO = new TagReponseDTO();
//            tagRepository.findById(tagId).ifPresent(tag -> {
//                tagReponseDTO.setId(tag.getId());
//                tagReponseDTO.setName(tag.getName());
//                tagReponseDTO.setDescription(tag.getDescription());
//                tagReponseDTO.setStatus(tag.getStatus());
//                tagReponseDTOList.add(tagReponseDTO);
//            });
//        }
//        productReponseDTO.setCategories(categoryReponseDTOList);
//        productReponseDTO.setTags(tagReponseDTOList);
//        return productReponseDTO;
//    }
//    public ProductReponseDTO createProdct(ProductRequestDTO requestDTO, List<Long> categoryId, List<Long> tagIdList) {
//        ProductEntity productEntity = new ProductEntity();
//        productEntity.setName(requestDTO.getName());
//        productEntity.setPrice(requestDTO.getPrice());
//        productEntity.setDescription(requestDTO.getDescription());
//        productEntity.setImage(requestDTO.getImage());
//        productEntity.setStatus(requestDTO.getStatus());
//        productRepository.save(productEntity);
//        List<Long> existingCategoryIdList = categoryRepository.findExistingCategoryIds(categoryId);
//        if(categoryId.size() != existingCategoryIdList.size()) {
//            throw new ResourceNotFoundException(MessageConst.LIST_CATEGORY_NOT_FOUND);
//        }
//        List<Long> existingTagIdList = tagRepository.findByExistingTag(existingCategoryIdList);
//        if(tagIdList.size() != existingTagIdList.size()) {
//            throw new ResourceNotFoundException(MessageConst.LIST_TAG_NOT_FOUND);
//        }
//        List<ProductCategoryEntity> productCategoryEntityList = new ArrayList<>();
//        for(Long cateLong : categoryId) {
//            ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
//            productCategoryEntity.setProductId(productEntity.getId());
//            productCategoryEntity.setCategoryId(cateLong);
//            productCategoryEntityList.add(productCategoryEntity);
//        }
//        List<ProductTagEntity> productTagEntityList = new ArrayList<>();
//        for (Long tag: tagIdList){
//            ProductTagEntity productTagEntity = new ProductTagEntity();
//            productTagEntity.setProductId(productEntity.getId());
//            productTagEntity.setTagId(tag);
//            productTagEntityList.add(productTagEntity);
//        }
//        productTagRepository.saveAll(productTagEntityList);
//        productCategoryRepository.saveAll(productCategoryEntityList);
//        return toReponseDTO(productEntity, categoryId, tagIdList);
//    }
//
//
//    public ProductReponseDTO updateProduct(ProductRequestDTO requestDTO, List<Long> categoryId, List<Long> tagIdList) {
//        ProductEntity productEntity = productRepository.findById(requestDTO.getId())
//                .orElseThrow(()->new ResourceNotFoundException(MessageConst.PRODUCT_NOT_FOUND));
//        if(requestDTO.getName() != null) {
//            productEntity.setName(requestDTO.getName());
//        }
//        if(requestDTO.getDescription() != null) {
//            productEntity.setDescription(requestDTO.getDescription());
//        }
//        if(requestDTO.getImage() != null) {
//            productEntity.setImage(requestDTO.getImage());
//        }
//        if (requestDTO.getPrice() != null) {
//            productEntity.setPrice(requestDTO.getPrice());
//        }
//        productRepository.save(productEntity);
//        if(categoryId != null && categoryId.isEmpty()){
//            List<Long> existingCaList = categoryRepository.findExistingCategoryIds(categoryId);
//            if (existingCaList.size() != categoryId.size()) {
//                throw new ResourceNotFoundException(MessageConst.LIST_CATEGORY_NOT_FOUND);
//            }
//        }
//        if(tagIdList != null && tagIdList.isEmpty()){
//            List<Long> existingTagList = tagRepository.findByExistingTag(tagIdList);
//            if (existingTagList.size() != tagIdList.size()) {
//                throw new ResourceNotFoundException(MessageConst.LIST_TAG_NOT_FOUND);
//            }
//        }
//
//        if(categoryId != null) {
//            productCategoryRepository.deleteByProductId(requestDTO.getId());
//            List<ProductCategoryEntity> productCategoryEntityList = new ArrayList<>();
//            for(Long cateLong : categoryId) {
//                ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
//                productCategoryEntity.setProductId(productEntity.getId());
//                productCategoryEntity.setCategoryId(cateLong);
//                productCategoryEntityList.add(productCategoryEntity);
//            }
//            productCategoryRepository.saveAll(productCategoryEntityList);
//        }
//        if(tagIdList != null) {
//            productTagRepository.deleteByProductId(requestDTO.getId());
//            List<ProductTagEntity> productTagEntityList = new ArrayList<>();
//            for(Long tagId : tagIdList) {
//                ProductTagEntity productTagEntity = new ProductTagEntity();
//                productTagEntity.setProductId(productEntity.getId());
//                productTagEntity.setTagId(tagId);
//                productTagEntityList.add(productTagEntity);
//            }
//            productTagRepository.saveAll(productTagEntityList);
//        }
//        return toReponseDTO(productEntity, categoryId != null ? categoryId : new ArrayList<>(), tagIdList != null ? tagIdList : new ArrayList<>());
//    }
//}
