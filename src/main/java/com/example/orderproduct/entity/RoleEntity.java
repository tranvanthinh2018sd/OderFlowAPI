package com.example.orderproduct.entity;

import com.example.orderproduct.constrant.TableConst;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.internal.build.AllowNonPortable;


@Getter
@Setter
@AllowNonPortable
@NoArgsConstructor
@Entity
@Table(name = TableConst.ROLE)
public class RoleEntity {

    @Id
    @SequenceGenerator(name = TableConst.SEQ_ROLE, sequenceName = TableConst.SEQ_ROLE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_ROLE)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private Long status;
}
