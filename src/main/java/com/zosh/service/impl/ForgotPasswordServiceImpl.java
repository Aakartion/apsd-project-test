package com.zosh.service.impl;

import com.zosh.domain.Verification_Type;
import com.zosh.model.ForgotPasswordToken;
import com.zosh.model.Users;
import com.zosh.repository.ForgotPasswordRepository;
import com.zosh.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;
    @Override
    public ForgotPasswordToken createForgotPasswordToken(Users user, String forgotPasswordTokenId, String otp, Verification_Type verificationType, String sendTo) {
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setUser(user);
        forgotPasswordToken.setSendTo(sendTo);
        forgotPasswordToken.setVerificationType(verificationType);
        forgotPasswordToken.setOtp(otp);
        forgotPasswordToken.setForgotPasswordTokenId(forgotPasswordTokenId);
        return forgotPasswordRepository.save(forgotPasswordToken);
    }

    @Override
    public ForgotPasswordToken findByForgotVerificationId(String id) {
        Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordRepository.findById(id);
        return forgotPasswordToken.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUserId(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteForgotPasswordToken(ForgotPasswordToken forgotPasswordToken) {
        forgotPasswordRepository.delete(forgotPasswordToken);
    }
}
