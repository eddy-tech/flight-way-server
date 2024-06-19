package org.intech.reservation.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidEntityException extends RuntimeException {
    @Getter
    private HttpStatus httpStatus;
    @Getter
    private List<String> errors;

    public InvalidEntityException(String message) { super(message);}

    public InvalidEntityException (String message, Throwable throwable){ super(message, throwable);}

    public InvalidEntityException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public InvalidEntityException(String message, Throwable throwable, HttpStatus httpStatus){
        super(message, throwable);
        this.httpStatus = httpStatus;
    }

    public InvalidEntityException(String message, HttpStatus httpStatus, List<String> errors){
        super(message);
        this.httpStatus = httpStatus;
        this.errors = errors;
        errors.forEach(System.out::println);
    }

    public InvalidEntityException(String message,List<String> errors){
        super(message);
        this.errors = errors;
        errors.forEach(System.out::println);
    }
}
