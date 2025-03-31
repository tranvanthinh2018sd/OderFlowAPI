package com.example.orderproduct.entity;

import com.example.orderproduct.constrant.TableConst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableConst.PASSWORD_RESET_TOKEN)
public class PasswordResetTokenEntity {
    @Id
    @SequenceGenerator(name = TableConst.SEQ_PASSWORD_RESET_TOKEN, sequenceName = TableConst.SEQ_PASSWORD_RESET_TOKEN, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConst.SEQ_PASSWORD_RESET_TOKEN)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TOKEN")
    private String token;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;
}
