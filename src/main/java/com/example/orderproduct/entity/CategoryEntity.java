package com.example.orderproduct.entity;

import com.example.orderproduct.constrant.TableConst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableConst.OD_CATEGORY)
public class CategoryEntity {

    @Id
    @SequenceGenerator(name = TableConst.SEQ_CATEGORY, sequenceName = TableConst.SEQ_CATEGORY, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_CATEGORY)
    @Column(name = "ID")
    private Long id;
    @Column(name ="NAME")
    private String name;
    @Column(name ="DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private Long status;
}
