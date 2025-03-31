package com.example.orderproduct.entity;

import com.example.orderproduct.constrant.TableConst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableConst.ROLE_MODULE)
public class RoleModuleEntity {
    @Id
    @SequenceGenerator(name = TableConst.SEQ_ROLE_MODULE, sequenceName = TableConst.SEQ_ROLE_MODULE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_ROLE_MODULE)
    @Column(name = "ID")
    private Long id;
    @Column(name ="ROLE_ID")
    private Long roleId;
    @Column(name = "MODULE_ID")
    private Long moduleId;
    @Column(name = "STATUS")
    private Long staus;
}
