package edu.rs.raf.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameExistsException extends CustomException{
    public UsernameExistsException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super("Username alrady in use", ErrorCode.USERNAME_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    public UsernameExistsException() {
        super("Username alrady in use", ErrorCode.USERNAME_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
}
