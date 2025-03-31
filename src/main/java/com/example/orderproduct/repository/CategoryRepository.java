//package com.example.orderproduct.repository;
//
//import com.example.orderproduct.entity.CategoryEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
//    @Query("SELECT c.id FROM CategoryEntity c WHERE c.id = :ids ")
//    List<Long> findExistingCategoryIds(@Param("ids") List<Long> ids);
//}
