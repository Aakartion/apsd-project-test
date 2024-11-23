package com.zosh.request;

import com.zosh.domain.Verification_Type;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {

    private String sendTo;
    private Verification_Type verificationType;
}
