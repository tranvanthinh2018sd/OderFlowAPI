package com.example.orderproduct.repository;

import com.example.orderproduct.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    @Query("SELECT t.id FROM TagEntity t WHERE t.id =: ids ")
    List<Long> findByExistingTag(@Param("ids") List<Long> ids);
}
