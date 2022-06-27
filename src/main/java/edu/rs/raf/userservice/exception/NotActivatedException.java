package edu.rs.raf.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotActivatedException extends CustomException{
    public NotActivatedException(String message) {
        super(message, ErrorCode.ACCOUNT_NOT_ACTIVATED_OR_BLOCKED, HttpStatus.FORBIDDEN);
    }
}
