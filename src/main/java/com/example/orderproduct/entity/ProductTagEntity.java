//package com.example.orderproduct.entity;
//
//import com.example.orderproduct.constrant.TableConst;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = TableConst.OD_PRODUCT_TAG)
//@Entity
//public class ProductTagEntity {
//
//    @Id
//    @SequenceGenerator(name = TableConst.SEQ_PRODUCT_TAG, sequenceName = TableConst.SEQ_PRODUCT_TAG,allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_PRODUCT_TAG)
//    @Column(name = "ID")
//    private Long id;
//    @Column(name = "PRODUCT_ID")
//    private Long productId;
//    @Column(name = "TAG_ID")
//    private Long tagId;
//    @Column(name = "STATUS")
//    private Long status;
//}
