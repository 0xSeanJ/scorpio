package top.jshanet.scorpio.security.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jshanet.scorpio.framework.dto.ScorpioResponse;
import top.jshanet.scorpio.security.exception.AccessDeniedException;
import top.jshanet.scorpio.security.exception.AuthenticationException;

/**
 * @author seanjiang
 * @since 2020-07-21
 */
@RestControllerAdvice
public class SecurityControllerAdvice {


    @ExceptionHandler(AccessDeniedException.class)
    public ScorpioResponse accessDenied(AccessDeniedException e) {
        return ScorpioResponse.fromException(e);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ScorpioResponse unauthorized(AuthenticationException e) {
        return ScorpioResponse.fromException(e);
    }
}
