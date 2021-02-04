package com.masmovil.userModule.dto;

public class OTPValidateResponse {

    private String otpString;

    public OTPValidateResponse(String otpString) {
        this.otpString = otpString;
    }

    public String getOtpString() {
        return otpString;
    }

    public void setOtpString(String otpString) {
        this.otpString = otpString;
    }
}
