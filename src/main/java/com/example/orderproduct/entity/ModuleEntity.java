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
@Table(name = TableConst.MODULE)
public class ModuleEntity {
    @Id
    @SequenceGenerator(name = TableConst.SEQ_MODULE, sequenceName = TableConst.SEQ_MODULE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_MODULE)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TITLE")
    private String title;
    @Column(name ="LINK")
    private String link;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ICON")
    private String icon;
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "STATUS")
    private Long staus; // 1 init, 2 deleted
}
