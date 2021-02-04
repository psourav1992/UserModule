package com.masmovil.userModule.exception;

public class WrongOTPValidation extends Exception {

    public WrongOTPValidation() {
        super();
    }


    public WrongOTPValidation(String message) {
        super(message);
    }


    public WrongOTPValidation(String message, Throwable cause) {
        super(message, cause);
    }
}
