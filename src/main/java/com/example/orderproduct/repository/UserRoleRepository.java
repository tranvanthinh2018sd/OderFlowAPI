package com.example.orderproduct.repository;

import com.example.orderproduct.entity.UserRoleEnity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleEnity, Long> {
    List<UserRoleEnity> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    @Query("SELECT a FROM UserRoleEnity a WHERE a.userId = :userId AND a.status = 1 ")
    Optional<UserRoleEnity> findUserRoleByUserId(Long userId);
}
