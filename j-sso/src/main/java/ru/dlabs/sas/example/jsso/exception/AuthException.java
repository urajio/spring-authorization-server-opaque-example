package ru.dlabs.sas.example.jsso.exception;

import org.springframework.security.core.AuthenticationException;
import ru.dlabs.sas.example.jsso.type.AuthErrorCode;

public class AuthException extends AuthenticationException {

    private final AuthErrorCode errorCode;

    public AuthException(AuthErrorCode errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
    }

    public AuthException(String msg, AuthErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public AuthException(AuthErrorCode errorCode) {
        super(null);
        this.errorCode = errorCode;
    }

    public AuthErrorCode getErrorCode() {
        return errorCode;
    }
}
