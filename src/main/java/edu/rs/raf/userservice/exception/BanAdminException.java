package edu.rs.raf.userservice.exception;

import org.springframework.http.HttpStatus;

public class BanAdminException extends CustomException{
    public BanAdminException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super("Email is use", ErrorCode.CANNOT_BAN_ADMIN, HttpStatus.FORBIDDEN);
    }

    public BanAdminException() {
        super("Email is use", ErrorCode.CANNOT_BAN_ADMIN, HttpStatus.FORBIDDEN);
    }
}
