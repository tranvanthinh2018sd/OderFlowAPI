package com.example.orderproduct.entity;

import com.example.orderproduct.constrant.TableConst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableConst.OD_PRODUCT_CATEGORY)
public class ProductCategoryEntity {

    @Id
    @SequenceGenerator(name = TableConst.SEQ_PRODUCT_CATEGORY, sequenceName = TableConst.SEQ_PRODUCT_CATEGORY, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_PRODUCT_CATEGORY)
    @Column(name ="ID")
    private Long id;
    @Column(name = "CATEGORY_ID")
    private Long categoryId;
    @Column(name = "PRODUCT_ID")
    private Long productId;
    @Column(name = "STATUS")
    private Long status;
}
