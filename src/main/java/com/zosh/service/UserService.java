package com.zosh.service;

import com.zosh.domain.Verification_Type;
import com.zosh.model.Users;

public interface UserService {

    Users findUserByJwt(String jwt) throws Exception;

    Users findUserByEmail(String email) throws Exception;

    Users findUserByUserId(Long userId) throws Exception;

    Users enableTwoFactorAuthentication(Verification_Type verificationType, String sendTo, Users user);

    Users updatePassword(Users user, String newPassword);


}
