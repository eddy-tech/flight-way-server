package org.intech.reservation.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends RuntimeException {
    @Getter
    private HttpStatus httpStatus;

    public EntityNotFoundException(String message) { super(message);}

    public EntityNotFoundException(String message, Throwable throwable) { super(message, throwable);}

    public EntityNotFoundException (String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public EntityNotFoundException (String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
