package com.zosh.controller;

import com.zosh.domain.Verification_Type;
import com.zosh.model.ForgotPasswordToken;
import com.zosh.model.Users;
import com.zosh.model.VerificationCode;
import com.zosh.request.ForgotPasswordTokenRequest;
import com.zosh.request.ResetPasswordRequest;
import com.zosh.response.ApiResponse;
import com.zosh.response.AuthResponse;
import com.zosh.service.EmailService;
import com.zosh.service.ForgotPasswordService;
import com.zosh.service.UserService;
import com.zosh.service.VerificationCodeService;
import com.zosh.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/profile")
    public ResponseEntity<Users> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        Users user = userService.findUserByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt,
                                                     @PathVariable Verification_Type verificationType) throws Exception {

        Users user = userService.findUserByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());
        if(verificationCode==null){
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if(verificationType.equals(Verification_Type.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }
        return new ResponseEntity<>("Verification OTP sent successfully", HttpStatus.OK);
    }

    @PatchMapping("/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<Users> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,
                                                               @PathVariable String otp) throws Exception {
        Users user = userService.findUserByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());
        String sendTo = verificationCode.getVerificationType().equals(Verification_Type.EMAIL)?verificationCode.getEmail():verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);
        if(isVerified){
            Users updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Wrong OTP");
    }


    @PostMapping("/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(@RequestBody ForgotPasswordTokenRequest forgotPasswordTokenRequest) throws Exception {
        Users user = userService.findUserByJwt(forgotPasswordTokenRequest.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String forgotPasswordTokeId = uuid.toString();

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findByUserId(user.getId());

        if(forgotPasswordToken == null){
            forgotPasswordToken = forgotPasswordService.createForgotPasswordToken(
                    user,
                    forgotPasswordTokeId,
                    otp,
                    forgotPasswordTokenRequest.getVerificationType(),
                    forgotPasswordTokenRequest.getSendTo()
            );
        }
        if(forgotPasswordToken == null){
            forgotPasswordToken = forgotPasswordService.createForgotPasswordToken(
                    user,
                    forgotPasswordTokeId,
                    otp,
                    forgotPasswordTokenRequest.getVerificationType(),forgotPasswordTokenRequest.getSendTo());
        }
        if(forgotPasswordTokenRequest.getVerificationType().equals(Verification_Type.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), forgotPasswordToken.getOtp());
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setSession(forgotPasswordToken.getForgotPasswordTokenId());
        authResponse.setMessage("Password reset OTP sent successfully");


        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PatchMapping("/reset-password/verify-otp/{otp}")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id,
                                               @RequestBody ResetPasswordRequest resetPasswordRequest,
                                               @RequestHeader("Authorization") String jwt) throws Exception {
        ForgotPasswordToken forgotPasswordToken =  forgotPasswordService.findByForgotVerificationId(id);
        boolean isVerified = forgotPasswordToken.getOtp().equals(resetPasswordRequest.getOtp());
        if(isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(), resetPasswordRequest.getPassword());
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Password update successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
        }
        throw new Exception("Wrong OTP");
    }



}
