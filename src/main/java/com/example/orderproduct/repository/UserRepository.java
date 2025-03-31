package com.example.orderproduct.repository;

import com.example.orderproduct.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query(value = "SELECT * FROM (" +
            "SELECT u.*, ROWNUM AS rnum FROM ( " +
            "SELECT * FROM OD_USER WHERE STATUS = 1 ORDER BY CREATE_DATE DESC " +
            ") u WHERE ROWNUM <= :offset + :size" +
            ") WHERE rnum > :offset", nativeQuery = true)
    List<UserEntity> findAll(@Param("offset") int offset, @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM OD_USER WHERE STATUS = 1" ,nativeQuery = true)
    int countUserPagin();

    @Query("SELECT a FROM UserEntity a WHERE a.username = :username AND a.status = 1 ")
    Optional<UserEntity> findByUserName(String username);

    @Query( value = "SELECT * FROM OD_USER  WHERE EMAIL = :email AND STATUS =1" , nativeQuery = true)
    Optional<UserEntity> findByEmail(@Param("email") String email);
}
