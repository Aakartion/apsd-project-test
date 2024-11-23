package com.zosh.service;

import com.zosh.domain.Verification_Type;
import com.zosh.model.Users;
import com.zosh.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(Users user, Verification_Type verification_type);

    VerificationCode getVerificationCodeByVerificationCodeId(Long verificationCodeId) throws Exception;

    VerificationCode getVerificationCodeByUserId(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);

//    boolean verifyOtp(String otp, VerificationCode verificationCode);


}
