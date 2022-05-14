package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (HttpStatus.UNAUTHORIZED)
public class PasswordNotMatchesException extends RuntimeException {
    public PasswordNotMatchesException() {
    }

    public PasswordNotMatchesException(String message) {
        super(message);
    }

    public PasswordNotMatchesException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordNotMatchesException(Throwable cause) {
        super(cause);
    }

    public PasswordNotMatchesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
