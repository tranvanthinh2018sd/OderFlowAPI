package com.example.orderproduct.entity;

import com.example.orderproduct.constrant.TableConst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableConst.OD_PRODUCT)
public class ProductEntity {

    @Id
    @SequenceGenerator(name = TableConst.SEQ_PRODUCT, sequenceName = TableConst.SEQ_PRODUCT, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_PRODUCT)
    @Column(name ="ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name ="PRICE")
    private Double price;
    @Column(name = "IMAGE")
    private String image;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private Long status;
    @Column(name ="USER_ID")
    private Long supplierId;
    @Column(name = "CREATE_DATE")
    private Date creaDate;
}
