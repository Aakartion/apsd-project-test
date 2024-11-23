package com.zosh.service.impl;

import com.zosh.config.JwtProvider;
import com.zosh.domain.Verification_Type;
import com.zosh.model.TwoFactorAuth;
import com.zosh.model.Users;
import com.zosh.repository.UserRepository;
import com.zosh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public Users findUserByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromJwtToken(jwt);

        Users user = userRepository.findByEmail(email);

        if(user == null){
            throw  new Exception("User not found");
        }
        return user;
    }

    @Override
    public Users findUserByEmail(String email) throws Exception {
        Users user = userRepository.findByEmail(email);

        if(user == null){
            throw  new Exception("User not found");
        }
        return user;
    }

    @Override
    public Users findUserByUserId(Long userId) throws Exception {
        Optional<Users> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw  new Exception("User not found");
        }
        return user.get();
    }

    @Override
    public Users enableTwoFactorAuthentication(Verification_Type verificationType, String sendTo, Users user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }


    @Override
    public Users updatePassword(Users user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
