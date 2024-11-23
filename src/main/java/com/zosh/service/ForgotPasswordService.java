package com.zosh.service;

import com.zosh.domain.Verification_Type;
import com.zosh.model.ForgotPasswordToken;
import com.zosh.model.Users;

public interface ForgotPasswordService {

    ForgotPasswordToken createForgotPasswordToken(Users user,
                                                  String forgotPasswordTokenId,
                                                  String otp,
                                                  Verification_Type verificationType,
                                                  String sendTo);

    ForgotPasswordToken findByForgotVerificationId(String id);

    ForgotPasswordToken findByUserId(Long userId);

    void deleteForgotPasswordToken(ForgotPasswordToken forgotPasswordToken);
}
