package com.masmovil.userModule.service;

import com.masmovil.userModule.dto.Role;
import com.masmovil.userModule.dto.User;
import com.masmovil.userModule.dto.UserOTPToken;
import com.masmovil.userModule.dto.UserRegistrationDTO;
import com.masmovil.userModule.exception.UserAlreadyExistException;
import com.masmovil.userModule.exception.WrongOTPValidation;
import com.masmovil.userModule.repository.UserOTPTokenRepository;
import com.masmovil.userModule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserModuleServiceImpl implements UserModuleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserOTPTokenRepository userOTPTokenRepository;



    @Override
    public User register(UserRegistrationDTO userRegistrationDTO) throws UserAlreadyExistException {

        //Let's check if user already registered with us
        if(checkIfUserExist(userRegistrationDTO.getEmail())){
            throw new UserAlreadyExistException("User already exists for this email");
        }

        User user = new User();
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setMobileNumber(userRegistrationDTO.getMobileNumber());
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        user.setStatus("Active");
        userRepository.save(user);
        return user;

    }

    @Override
    public User getUserbyUsername(String userName) throws UserAlreadyExistException {
        return userRepository.findByEmail(userName);
    }

    @Override
    public String validateOTP(String userName, String otpString) throws WrongOTPValidation {
        String response = null;
        User user = userRepository.findByEmail(userName);
        UserOTPToken userOTPToken = userOTPTokenRepository.findByUserId(user.getAuth_user_id());

        Timestamp expiredTimestamp = userOTPToken.getExpiryDate();
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());

        if(!userOTPToken.getOtpToken().toString().equalsIgnoreCase(otpString)){
            throw new WrongOTPValidation("Entered OTP is wrong");
        }else if(!currentTimestamp.before(expiredTimestamp)){
            userOTPTokenRepository.updateUserOTPToken(user.getAuth_user_id());
            throw new WrongOTPValidation("Your OTP has been expired");
        }else {
            userOTPTokenRepository.updateUserOTPToken(user.getAuth_user_id());
            response = "success";
        }

        return response;

    }

    @Override
    public void createOTPandSent(User user) {

        String otpNumber = createOTP();
        //save the OTP to database
        Timestamp timestamp = new Timestamp(new Date().getTime());
        System.out.println(timestamp);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());

        // add 60 seconds
        cal.add(Calendar.SECOND, 60);
        timestamp = new Timestamp(cal.getTime().getTime());
        System.out.println(timestamp);
        userOTPTokenRepository.updateUserOTPToken(user.getAuth_user_id());
        UserOTPToken userOTPToken = new UserOTPToken(user.getAuth_user_id(), otpNumber,"ACTIVE", timestamp);
        userOTPTokenRepository.save(userOTPToken);

        //create a service to send OTP token to mobile Number

    }

    private String createOTP() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    private boolean checkIfUserExist(String email) {
        return userRepository.findByEmail(email) !=null ? true : false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password");
        }


        System.out.println(user.getEmail()+ " **** "+ user.getPassword()+" **** "+ user.getStatus() );
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

    }
}
