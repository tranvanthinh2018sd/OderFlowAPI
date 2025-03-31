package com.example.orderproduct.entity;

import com.example.orderproduct.constrant.TableConst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = TableConst.USER_ROLE)
@Entity
public class UserRoleEnity {
    @Id
    @SequenceGenerator(name = TableConst.SEQ_USER_ROLE, sequenceName = TableConst.SEQ_USER_ROLE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_USER_ROLE)
    @Column(name = "ID")
    private Long id;
    @Column(name ="USER_ID")
    private Long userId;
    @Column(name = "ROLE_ID")
    private Long roleId;
    @Column(name = "STATUS")
    private Long status;
}
