package com.masmovil.userModule.controller;

import com.masmovil.userModule.dto.*;
import com.masmovil.userModule.exception.UserAlreadyExistException;
import com.masmovil.userModule.exception.WrongOTPValidation;
import com.masmovil.userModule.service.UserModuleService;
import com.masmovil.userModule.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080",maxAge = 3600)
@RestController
public class UserModuleController {

    @Autowired
    UserModuleService userModuleService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/getUserDetails")
    public User getUserDetails(@RequestHeader("Authorization") String authorizationHeader){

        String userName = null;
        String jwt = null;

        if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(jwt);
        }

        User user = new User();


        try {
                if(userName!=null) {
                    user = userModuleService.getUserbyUsername(userName);
                }
        } catch (UserAlreadyExistException e) {
            e.printStackTrace();
        }

        return user;
    }

    @PostMapping("/register-process")
    User registerNewUser(@RequestBody UserRegistrationDTO userRegistrationDTO) throws UserAlreadyExistException {

        return userModuleService.register(userRegistrationDTO);
    }

    @PostMapping("/login")
    ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));

        }catch (BadCredentialsException e){
            throw new Exception("Incorrect UserName or Password",e);
        }
        User user = userModuleService.getUserbyUsername(authenticationRequest.getEmail());
        userModuleService.createOTPandSent(user);
        final UserDetails userDetails = userModuleService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt,"success",user));

    }

    @PostMapping("/validateOTP")
    ResponseEntity<?> validateOTP(@RequestHeader("Authorization") String authorizationHeader,@RequestBody OTPRequest otpRequest) throws  WrongOTPValidation {

        System.out.println("OTP String "+otpRequest.getOtpString());
        //return userModuleService.register(userRegistrationDTO);
        String userName = null;
        String jwt = null;

        if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(jwt);
        }

        String response = userModuleService.validateOTP(userName, otpRequest.getOtpString());
        return ResponseEntity.ok(new OTPValidateResponse(response));
    }

}
