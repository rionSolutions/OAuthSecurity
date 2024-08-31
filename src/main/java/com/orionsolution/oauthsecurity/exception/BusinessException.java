package com.orionsolution.oauthsecurity.exception;


import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class BusinessException {

    public static class HandlerException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private String message;
        private HttpStatus status;

        public HandlerException(String message, HttpStatus status) {
            this.message = message;
            this.status = status;
        }

    }

    @ExceptionHandler(HandlerException.class)
    public ResponseEntity<Error> businessException(HandlerException ex) {
        return new ResponseEntity<>(new Error(ex.message, ex.status.value()), ex.status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Error> dataException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(new Error("Cannot process, because should have use valid and unique credential",
                HttpStatus.UNPROCESSABLE_ENTITY.value()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Error> jwtExpirated(ExpiredJwtException ex) {
        return new ResponseEntity<>(new Error(ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }

}
