package com.example.orderproduct.repository;

import com.example.orderproduct.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {
    @Query("select m.id from ModuleEntity m where m.id = :ids")
    List<Long> existingModule(@Param("ids") List<Long> ids);
}
