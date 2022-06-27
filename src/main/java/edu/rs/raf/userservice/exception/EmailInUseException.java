package edu.rs.raf.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailInUseException extends CustomException{
    public EmailInUseException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super("Email is use", ErrorCode.EMAIL_ALREADY_IN_USE, HttpStatus.CONFLICT);
    }

    public EmailInUseException() {
        super("Email is use", ErrorCode.EMAIL_ALREADY_IN_USE, HttpStatus.CONFLICT);
    }
}
