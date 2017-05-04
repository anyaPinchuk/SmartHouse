package exception;

import org.springframework.security.core.AuthenticationException;

public class WrongCredentialsException extends AuthenticationException {
    public WrongCredentialsException(String msg) {
        super(msg);
    }
}
