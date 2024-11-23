package com.zosh.service.impl;

import com.zosh.model.TwoFactorOTP;
import com.zosh.model.Users;
import com.zosh.repository.TwoFactorOtpRepository;
import com.zosh.service.TwoFactorOtpService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {

    private TwoFactorOtpRepository twoFactorOtpRepository;

    public TwoFactorOtpServiceImpl(TwoFactorOtpRepository twoFactorOtpRepository) {
        this.twoFactorOtpRepository = twoFactorOtpRepository;
    }

    @Override
    public TwoFactorOTP createTwoFactorOtp(Users user, String otpCode, String jwtToken) {
        UUID uuid = UUID.randomUUID();
        String twoFactorOtpId = uuid.toString();

        TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
        twoFactorOTP.setTwoFactorOtpId(twoFactorOtpId);
        twoFactorOTP.setOtpCode(otpCode);
        twoFactorOTP.setJwtToken(jwtToken);
        twoFactorOTP.setUser(user);
        return twoFactorOtpRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUserId(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findByOtpId(String twoFactorOtpId) {
        Optional<TwoFactorOTP> searchOtp = twoFactorOtpRepository.findById(twoFactorOtpId);

        return searchOtp.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {

        return twoFactorOTP.getOtpCode().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {
        twoFactorOtpRepository.delete(twoFactorOTP);
    }
}
