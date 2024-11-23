package com.zosh.model;

import com.zosh.domain.Verification_Type;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long verificationCodeId;
    private String otp;
    @OneToOne
    private Users user;
    private String email;
    private String mobile;
    private Verification_Type verificationType;

}
