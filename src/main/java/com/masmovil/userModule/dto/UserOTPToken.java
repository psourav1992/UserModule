package com.masmovil.userModule.dto;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user_otp_token")
public class UserOTPToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_token_id;

    @Column(nullable = false, length = 64)
    private Long userId;

    @Column(nullable = false, length = 64)
    private String otpToken;

    @Column(nullable = false, length = 64)
    private String status;

    @Column(nullable = false, length = 64)
    private java.sql.Timestamp expiryDate;

    public UserOTPToken() {
    }

    public UserOTPToken(Long userId, String otpToken, Timestamp expiryDate) {
        this.userId = userId;
        this.otpToken = otpToken;
        this.expiryDate = expiryDate;
    }

    public UserOTPToken(Long userId, String otpToken, String status, Timestamp expiryDate) {
        this.userId = userId;
        this.otpToken = otpToken;
        this.status = status;
        this.expiryDate = expiryDate;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUser_token_id() {
        return user_token_id;
    }

    public void setUser_token_id(Long user_token_id) {
        this.user_token_id = user_token_id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getOtpToken() {
        return otpToken;
    }

    public void setOtpToken(String otpToken) {
        this.otpToken = otpToken;
    }


}
