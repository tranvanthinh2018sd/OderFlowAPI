package com.example.orderproduct.repository;

import com.example.orderproduct.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query("select r.id from RoleEntity r where r.id IN :ids ")
    List<Long> existingRoles(@Param("ids") List<Long> ids);

    @Query(value = "SELECT * FROM (" +
            "SELECT u.*, ROWNUM AS rnum FROM (" +
            "SELECT * FROM OD_ROLE WHERE STATUS = 1 " +
            "AND (:search IS NULL OR LOWER(name) LIKE LOWER('%' || :search || '%')) " +
            "ORDER BY id DESC " +
            ") u WHERE ROWNUM <= :offset + :size" +
            ") WHERE rnum > :offset", nativeQuery = true)
    List<RoleEntity> findAll(@Param("search") String search, @Param("offset") int offset, @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM OD_ROLE WHERE STATUS = 1" +
            "AND (:search IS NULL OR LOWER(name) LIKE LOWER('%' || :search || '%'))", nativeQuery = true)
    int countPagin(@Param("search") String search);

    @Query("SELECT a FROM RoleEntity a " +
            "JOIN UserRoleEnity b ON b.roleId = a.id " +
            "JOIN UserEntity c ON c.id = b.userId " +
            "WHERE c.id = :id AND a.status = 1")
    Optional<RoleEntity> findByRoleId(Long id);

    @Query("SELECT r FROM RoleEntity r " +
            "JOIN UserRoleEnity ur ON ur.roleId = r.id " +
            "WHERE ur.userId = :userId")
    List<RoleEntity> findRolesByUserId(@Param("userId") Long userId);



}
