package com.example.orderproduct.repository;

import com.example.orderproduct.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "SELECT * FROM (" +
            "SELECT u.*, ROWNUM AS rnum FROM ( " +
            "SELECT * FROM OD_PRODUCT WHERE STATUS = 1 ORDER BY CREATE_DATE DESC " +
            ") u WHERE ROWNUM <= :offset + :size" +
            ") WHERE rnum > :offset", nativeQuery = true)
    List<ProductEntity> findAll(@Param("offset") int offset, @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM OD_PRODUCT WHERE STATUS = 1",nativeQuery = true)
    int countPagin();
}
