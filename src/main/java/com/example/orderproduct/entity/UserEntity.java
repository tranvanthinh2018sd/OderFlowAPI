package com.example.orderproduct.entity;

import com.example.orderproduct.constrant.TableConst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableConst.OD_USER)
public class UserEntity {
    @Id
    @SequenceGenerator(name = TableConst.SEQ_USER, sequenceName = TableConst.SEQ_USER, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_USER)
    @Column(name = "ID")
    private long id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "IMAGE")
    private String image;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "CREATE_DATE")
    private Date creaDate;
    @Column(name = "UPDATE_DATE")
    private Date updateDate;
    @Column(name = "STATUS")
    private Long status; // 1 init, 2 deleted
}
