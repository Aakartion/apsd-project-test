package com.zosh.service.impl;

import com.zosh.domain.Verification_Type;
import com.zosh.model.Users;
import com.zosh.model.VerificationCode;
import com.zosh.repository.VerificationCodeRepository;
import com.zosh.service.VerificationCodeService;
import com.zosh.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Override
    public VerificationCode sendVerificationCode(Users user, Verification_Type verification_type) {
        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generateOtp());
        verificationCode1.setVerificationType(verification_type);
        verificationCode1.setUser(user);
        return verificationCodeRepository.save(verificationCode1);
    }

    @Override
    public VerificationCode getVerificationCodeByVerificationCodeId(Long verificationCodeId) throws Exception {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(verificationCodeId);
        if(verificationCode.isPresent()){
            return verificationCode.get();
        }
        throw new Exception("Verification not found");
    }

    @Override
    public VerificationCode getVerificationCodeByUserId(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
}
