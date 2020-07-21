package top.jshanet.scorpio.security.exception;

import top.jshanet.scorpio.framework.exception.ScorpioException;
import top.jshanet.scorpio.security.status.SecurityStatus;

/**
 * @author seanjiang
 * @since 2020-07-21
 */
public class AuthenticationException extends ScorpioException {
    public AuthenticationException() {
        super(SecurityStatus.UNAUTHORIZED_CLIENT);
    }
}
