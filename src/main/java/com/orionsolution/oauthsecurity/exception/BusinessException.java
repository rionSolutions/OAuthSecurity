package com.orionsolution.oauthsecurity.exception;


import com.orionsolution.oauthsecurity.model.ErrorDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
    public ResponseEntity<ErrorDTO> BusinessException(HandlerException ex) {
        return new ResponseEntity<>(new ErrorDTO(ex.message, ex.status.value()), ex.status);
    }
}
