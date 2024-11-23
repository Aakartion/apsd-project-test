package com.zosh.model;

import com.zosh.domain.Verification_Type;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String forgotPasswordTokenId;
    @OneToOne
    private Users user;

    private String otp;

    private Verification_Type verificationType;

    private String sendTo;
}
