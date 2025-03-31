//package com.example.orderproduct.entity;
//
//import com.example.orderproduct.constrant.TableConst;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Setter
//@Getter
//@NoArgsConstructor
//@Table(name =TableConst.OD_TAG)
//@Entity
//public class TagEntity {
//
//    @Id
//    @SequenceGenerator(name = TableConst.SEQ_TAG, sequenceName = TableConst.SEQ_TAG,allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_TAG)
//    @Column(name = "ID")
//    private Long id;
//    @Column(name = "NAME")
//    private String name;
//    @Column(name = "DESCRIPTION")
//    private String description;
//    @Column(name = "STATUS")
//    private Long status;
//}
