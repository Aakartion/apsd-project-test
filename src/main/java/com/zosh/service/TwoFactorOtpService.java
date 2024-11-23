package com.zosh.service;

import com.zosh.model.TwoFactorOTP;
import com.zosh.model.Users;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwoFactorOtp(Users user, String otpCode, String jwtToken);

    TwoFactorOTP findByUserId(Long userId);

    TwoFactorOTP findByOtpId(String twoFactorOtpId);

    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);
}
