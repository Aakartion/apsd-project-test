package com.zosh.controller;

import com.zosh.config.JwtProvider;
import com.zosh.model.TwoFactorOTP;
import com.zosh.model.Users;
import com.zosh.repository.UserRepository;
import com.zosh.response.AuthResponse;
import com.zosh.service.EmailService;
import com.zosh.service.TwoFactorOtpService;
import com.zosh.service.impl.CustomUserDetailsService;
import com.zosh.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody Users user) throws Exception {
        Users isEmailExist = userRepository.findByEmail(user.getEmail());
        if(isEmailExist!=null){
            throw new Exception("Email is already used with another account");
        }

        Users newUser = new Users();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        Users savedUser =  userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Register success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }


    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> login(@RequestBody Users user) throws Exception {

        String username = user.getEmail();
        String password = user.getPassword();


        Authentication authentication = authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = JwtProvider.generateToken(authentication);

        Users userFromDB = userRepository.findByEmail(username);

        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two Factor auth is enabled");
            res.setTwoFactorAuthenticationEnabled(true);
            String otp = OtpUtils.generateOtp();

            TwoFactorOTP oldTwoFactorOtp = twoFactorOtpService.findByUserId(userFromDB.getId());
            if(oldTwoFactorOtp!=null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }

            TwoFactorOTP newFactorOTP = twoFactorOtpService.createTwoFactorOtp(userFromDB, otp, jwtToken);

            emailService.sendVerificationOtpEmail(username, otp);

            res.setSession(newFactorOTP.getTwoFactorOtpId());
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }

        AuthResponse res = new AuthResponse();
        res.setJwt(jwtToken);
        res.setStatus(true);
        res.setMessage("Login success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid username");
        }
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp,
                                                        @RequestParam String id) throws Exception {

        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findByOtpId(id);

        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP, otp)){
            AuthResponse response = new AuthResponse();
            response.setMessage("Two factor authentication verified");
            response.setTwoFactorAuthenticationEnabled(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new Exception("Invalid OTP");
    }
}
