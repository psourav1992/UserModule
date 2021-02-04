package com.masmovil.userModule.service;

import com.masmovil.userModule.dto.User;
import com.masmovil.userModule.dto.UserRegistrationDTO;
import com.masmovil.userModule.exception.UserAlreadyExistException;
import com.masmovil.userModule.exception.WrongOTPValidation;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserModuleService extends UserDetailsService {

    public User register(UserRegistrationDTO userRegistrationDTO) throws UserAlreadyExistException;
    public User getUserbyUsername(String userName) throws UserAlreadyExistException;

    String validateOTP(String userName, String otpString) throws WrongOTPValidation;

    void createOTPandSent(User user);
}
