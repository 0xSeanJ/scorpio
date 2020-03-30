package top.jshanet.scorpio.framework.common.exception;


import org.springframework.security.core.AuthenticationException;

public class ScorpioAuthenticationException extends AuthenticationException {

    public ScorpioAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public ScorpioAuthenticationException(String msg) {
        super(msg);
    }
}
